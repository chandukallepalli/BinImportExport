package com.example.poc.controller;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import com.example.poc.dto.ResponseDto;
import com.example.poc.repository.Message17Repository;
import com.example.poc.repository.Message17Repository.StoredMessageData;
import com.example.poc.service.MessageService;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

@RestController
public class messageController {

    @Autowired
    private MessageService csvService;
    @Autowired
    private Message17Repository message17Repository;

    private static final int HEADER_VALUE = 0xAAAA;
    private static final int HEADER_SIZE = 2;
    private static final int MESSAGE_TYPE_SIZE = 1;
    private static final int LENGTH_FIELD_SIZE = 2;
    private static final int MINIMUM_MESSAGE_SIZE = HEADER_SIZE + MESSAGE_TYPE_SIZE + LENGTH_FIELD_SIZE;
    
    private static final Logger logger = LoggerFactory.getLogger(messageController.class);
    private static final ExecutorService executor = Executors.newCachedThreadPool();
    private static final int BATCH_SIZE = 1000;

    @PostMapping("/upload")
    public String uploadFile(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return "File upload failed: Empty file.";
        }

        long startTime = System.currentTimeMillis();
        try {
            byte[] fileBytes = file.getBytes();
            logger.info("File size: {} bytes", fileBytes.length);
            if (fileBytes.length >= 10) {
                logger.info("First 10 bytes: {}", bytesToHex(Arrays.copyOfRange(fileBytes, 0, 10)));
            }

            List<byte[]> parsedMessages = parseMessages(fileBytes);  
            
            if (parsedMessages.isEmpty()) {
                return "File uploaded, but no valid messages were found.";
            }
            
            processMessagesInBatches(parsedMessages);

        } catch (Exception e) {
            logger.error("Error processing file", e);
            return "Error processing file: " + e.getMessage();
        }

        long endTime = System.currentTimeMillis();
        long processingTime = endTime - startTime;
        
        if (processingTime > 2000) {
            return "Process took too long! (" + processingTime + " ms)";
        }
        return "File processed successfully in " + processingTime + " ms.";
    }

    private List<byte[]> parseMessages(byte[] fileBytes) {
        List<byte[]> messages = new ArrayList<>();
        int currentPosition = 0;

        logger.debug("Starting message parsing. File size: {} bytes", fileBytes.length);

        while (currentPosition < fileBytes.length - MINIMUM_MESSAGE_SIZE) {
            if (currentPosition % 1000 == 0) {
                logger.debug("Parsing position: {}", currentPosition);
            }
            int header = ((fileBytes[currentPosition] & 0xFF) << 8) | 
                        (fileBytes[currentPosition + 1] & 0xFF);

            if (header == HEADER_VALUE) {
                logger.debug("Found header at position: {}", currentPosition);
        
                int messageType = fileBytes[currentPosition + 2] & 0xFF;
                int messageLength = ((fileBytes[currentPosition + 3] & 0xFF) << 8) |
                                  (fileBytes[currentPosition + 4] & 0xFF);
                
                logger.debug("Message type: {}, length: {}", messageType, messageLength);

                if (messageLength > 0 && 
                    currentPosition + HEADER_SIZE + MESSAGE_TYPE_SIZE + LENGTH_FIELD_SIZE + messageLength <= fileBytes.length) {
                    
                    int totalMessageSize = HEADER_SIZE + MESSAGE_TYPE_SIZE + LENGTH_FIELD_SIZE + messageLength;
                    byte[] completeMessage = Arrays.copyOfRange(fileBytes, currentPosition, 
                                                             currentPosition + totalMessageSize);

                    messages.add(completeMessage);
                    logger.debug("Added message. Total messages: {}", messages.size());
                    
                    currentPosition += totalMessageSize;
                    continue;
                } else {
                    logger.debug("Invalid message length: {}", messageLength);
                }
            }
            currentPosition++;
        }
        return messages;
    }

    private void processMessagesInBatches(List<byte[]> parsedMessages) {
        long startProcessingTime = System.currentTimeMillis();
        int totalMessages = parsedMessages.size();
        
        for (int i = 0; i < totalMessages; i += BATCH_SIZE) {
            int endIndex = Math.min(i + BATCH_SIZE, totalMessages);
            List<byte[]> batch = parsedMessages.subList(i, endIndex);
            
            List<CompletableFuture<Void>> futures = batch.stream()
                .map(message -> CompletableFuture.runAsync(() -> {
                    logger.debug("Processing message of size: {}", message.length);
                    csvService.decodeHexData(message);
                }, executor))
                .collect(Collectors.toList());

            CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();
        }

        long endProcessingTime = System.currentTimeMillis();
        logger.info("Processed {} messages in {} ms", 
                   (endProcessingTime - startProcessingTime));
    }

  @PostMapping("/uploadBinFile")
public String uploadBinFile(@RequestParam("file") MultipartFile file) {
    try {
        long startTime = System.currentTimeMillis();
        byte[] fileBytes = file.getBytes();
        CompletableFuture.runAsync(() -> csvService.decodeHexData(fileBytes));
        long endTime = System.currentTimeMillis();
        System.out.println("File upload duration: " + (endTime - startTime) + "ms");
        return "File uploaded and processing started!";
    } catch (Exception e) {
        e.printStackTrace();
        return "File upload failed: " + e.getMessage();
    }
} 
    @PostMapping("/messages")
    public List<StoredMessageData> getMessagesByDateRange(@RequestBody
        ResponseDto request) {
            String fromDateTime = request.getFromDate();
            String toDateTime = request.getToDate();
        return message17Repository.filterByDateTimeRange(fromDateTime, toDateTime);
    }

    @PostMapping("/filter")
    public List<StoredMessageData> filterMessages(@RequestBody ResponseDto filterRequest) {

        return message17Repository.filterData(
            filterRequest.getFromDate(),
            filterRequest.getToDate(),
            filterRequest.getPageSize(),
            filterRequest.getPage(),
            filterRequest.getFilters()
        );
    }



    @PostMapping("/saveAll")
    public ResponseEntity<ByteArrayResource> downloadAllColumns(
            @RequestBody ResponseDto request,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "25") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDirection) {
        try {
            String fromDate = request.getFromDate();
            String toDate = request.getToDate();

            // logger.info("Received request for full Excel download with fromDate: {}, toDate: {}", fromDate, toDate);

            // Generate the full Excel file
            ByteArrayResource resource = message17Repository.generateFullExcel(fromDate, toDate);

            // Set the headers to trigger file download and give a custom filename
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=full_report.xlsx")
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .contentLength(resource.contentLength())
                    .body(resource);
        } catch (Exception e) {
            // logger.error("Error while downloading all columns: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ByteArrayResource("Failed to generate Excel file".getBytes()));
        }
    }
    public static String bytesToHex(byte[] bytes) {
        StringBuilder hexString = new StringBuilder();
        for (byte b : bytes) {
            String hex = Integer.toHexString(0xFF & b); // Mask byte to extract lower 8 bits
            if (hex.length() == 1) {
                hexString.append('0'); // Append leading zero for single-digit hex
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }
    @GetMapping("size")
    public int getSize(){
        return csvService.getCacheSize();
    }

}
    
    

 



