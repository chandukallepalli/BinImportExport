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
    private static final int BATCH_SIZE = 5000;
    
    private static final Logger logger = LoggerFactory.getLogger(messageController.class);
    private static final ExecutorService executor = Executors.newFixedThreadPool(
        Runtime.getRuntime().availableProcessors() * 2,
        r -> {
            Thread t = new Thread(r);
            t.setPriority(Thread.MAX_PRIORITY);
            return t;
        }
    );

    @PostMapping("/upload")
    public String uploadFile(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return "File upload failed: Empty file.";
        }

        long startTime = System.currentTimeMillis();
        try {
            byte[] fileBytes = file.getBytes();
            logger.info("File size: {} bytes", fileBytes.length);
            List<byte[]> parsedMessages = parseMessages(fileBytes);  
            
            if (parsedMessages.isEmpty()) {
                return "File uploaded, but no valid messages were found.";
            }
            
            int totalMessages = parsedMessages.size();
            int numThreads = Runtime.getRuntime().availableProcessors() * 2;
            int messagesPerThread = totalMessages / numThreads;
            
            List<CompletableFuture<Void>> futures = new ArrayList<>();
            
            for (int i = 0; i < numThreads; i++) {
                int startIdx = i * messagesPerThread;
                int endIdx = (i == numThreads - 1) ? totalMessages : (i + 1) * messagesPerThread;
                
                futures.add(CompletableFuture.runAsync(() -> {
                    for (int j = startIdx; j < endIdx; j += BATCH_SIZE) {
                        int batchEnd = Math.min(j + BATCH_SIZE, endIdx);
                        List<byte[]> batch = parsedMessages.subList(j, batchEnd);
                        batch.forEach(message -> csvService.decodeHexData(message));
                    }
                }, executor));
            }

            CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();

        } catch (Exception e) {
            logger.error("Error processing file", e);
            return "Error processing file: " + e.getMessage();
        }

        long processingTime = System.currentTimeMillis() - startTime;
        return "File processed in " + processingTime + " ms";
    }

    private List<byte[]> parseMessages(byte[] fileBytes) {
        List<byte[]> messages = new ArrayList<>();
        int currentPosition = 0;

        while (currentPosition < fileBytes.length - MINIMUM_MESSAGE_SIZE) {
            int header = ((fileBytes[currentPosition] & 0xFF) << 8) | 
                        (fileBytes[currentPosition + 1] & 0xFF);

            if (header == HEADER_VALUE) {
                int messageType = fileBytes[currentPosition + 2] & 0xFF;
                int messageLength = ((fileBytes[currentPosition + 3] & 0xFF) << 8) |
                                  (fileBytes[currentPosition + 4] & 0xFF);

                if (messageLength > 0 && 
                    currentPosition + HEADER_SIZE + MESSAGE_TYPE_SIZE + LENGTH_FIELD_SIZE + messageLength <= fileBytes.length) {
                    
                    int totalMessageSize = HEADER_SIZE + MESSAGE_TYPE_SIZE + LENGTH_FIELD_SIZE + messageLength;
                    byte[] completeMessage = Arrays.copyOfRange(fileBytes, currentPosition, 
                                                             currentPosition + totalMessageSize);
                    messages.add(completeMessage);
                    currentPosition += totalMessageSize;
                    continue;
                }
            }
            currentPosition++;
        }
        return messages;
    }

    @PostMapping("/uploadBinFile")
    public String uploadBinFile(@RequestParam("file") MultipartFile file) {
        try {
            long startTime = System.currentTimeMillis();
            byte[] fileBytes = file.getBytes();
            CompletableFuture.runAsync(() -> csvService.decodeHexData(fileBytes), executor);
            long endTime = System.currentTimeMillis();
            return "File uploaded and processing started! Duration: " + (endTime - startTime) + "ms";
        } catch (Exception e) {
            logger.error("File upload failed", e);
            return "File upload failed: " + e.getMessage();
        }
    }

    @PostMapping("/messages")
    public List<StoredMessageData> getMessagesByDateRange(@RequestBody ResponseDto request) {
        return message17Repository.filterByDateTimeRange(request.getFromDate(), request.getToDate());
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
            ByteArrayResource resource = message17Repository.generateFullExcel(
                request.getFromDate(), request.getToDate());
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=full_report.xlsx")
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .contentLength(resource.contentLength())
                    .body(resource);
        } catch (Exception e) {
            logger.error("Error generating Excel file", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ByteArrayResource("Failed to generate Excel file".getBytes()));
        }
    }

    @GetMapping("size")
    public int getSize() {
        return csvService.getCacheSize();
    }

    private static String bytesToHex(byte[] bytes) {
        StringBuilder hexString = new StringBuilder();
        for (byte b : bytes) {
            String hex = Integer.toHexString(0xFF & b);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }
}