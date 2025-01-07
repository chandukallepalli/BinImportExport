package com.example.poc.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.poc.dto.Message17Request;
import com.example.poc.entity.HeartBeatMessage;
import com.example.poc.entity.Message17;
import com.example.poc.service.MessageService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@RestController
public class messageController {

    @Autowired
    private MessageService messageService;

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
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("File upload failed: Empty file.");
        }

        long startTime = System.currentTimeMillis();
        int totalMessages = 0;

        try {
            byte[] fileBytes = file.getBytes();
            logger.info("File size: {} bytes", fileBytes.length);

            List<byte[]> parsedMessages = parseMessages(fileBytes);//1

            if (parsedMessages.isEmpty()) {
                return ResponseEntity.status(HttpStatus.OK).body("File uploaded, but no valid messages were found.");
            }

            totalMessages = parsedMessages.size();
            logger.info("Total messages parsed: {}", totalMessages);

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
                        batch.forEach(messageService::decodeHexData);
                    }
                }, executor));
            }

            CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();
        } catch (Exception e) {
            logger.error("Error processing file", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error processing file: " + e.getMessage());
        }

        long processingTime = System.currentTimeMillis() - startTime;
        logger.info("File processed successfully. Total messages: {}, Time taken: {} ms", totalMessages, processingTime);

        return ResponseEntity.status(HttpStatus.OK)
                .body("File processed successfully. Total messages: " + totalMessages + ", Time taken: " + processingTime + " ms");
    }

    private List<byte[]> parseMessages(byte[] fileBytes) {
        List<byte[]> messages = new ArrayList<>();
        int currentPosition = 0;

        while (currentPosition < fileBytes.length - 1) {
            int header = ((fileBytes[currentPosition] & 0xFF) << 8) |
                        (fileBytes[currentPosition + 1] & 0xFF);

            if (header == HEADER_VALUE) {
                int messageStartPosition = currentPosition;

                if (currentPosition + LENGTH_FIELD_SIZE > fileBytes.length) {
                    currentPosition = messageStartPosition + 1;
                    continue;
                }

                int messageLength = ((fileBytes[currentPosition + 3] & 0xFF) << 8) |
                                  (fileBytes[currentPosition + 4] & 0xFF);

                if (messageLength <= 0 || currentPosition + messageLength > fileBytes.length) {
                    currentPosition = messageStartPosition + 1;
                    continue;
                }

                int totalMessageSize = HEADER_SIZE + messageLength;
                byte[] completeMessage = Arrays.copyOfRange(fileBytes, messageStartPosition, messageStartPosition + totalMessageSize);

                messages.add(completeMessage);
                currentPosition = currentPosition + messageLength + 2;
            } else {
                currentPosition++;
            }
        }

        return messages;
    }
    @PostMapping("/message17")
public ResponseEntity<List<HeartBeatMessage>> getMessage17(
        @RequestBody Message17Request request) {
    // try {
        // if (request.getFromDate() == null || request.getToDate() == null) {
        //     return ResponseEntity.badRequest()
        //         .body(Map.of("error", "From date and to date are required"));
        // }

        // if (request.getPage() < 0) {
        //     return ResponseEntity.badRequest()
        //         .body(Map.of("error", "Page number cannot be negative"));
        // }

        // if (request.getPageSize() <= 0) {
        //     return ResponseEntity.badRequest()
        //         .body(Map.of("error", "Page size must be greater than 0"));
        // }
        List<HeartBeatMessage> result = messageService.getMessagesByDateRange(
            request.getFromDate(),
            request.getToDate(),
            request.getPage(),
            request.getPageSize()
            // request.getFilters()
        );

        return ResponseEntity.ok(result);

    
    // } catch (Exception e) {
    //     return e;
    // }
}

}
