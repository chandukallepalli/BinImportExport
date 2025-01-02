package com.example.poc.service;


import com.example.poc.entity.Message17;
import com.example.poc.util.CommonFile;

import com.example.poc.util.StationaryHealthInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import jakarta.annotation.PreDestroy;
import jakarta.transaction.Transactional;
@Service
public class MessageService {

    private static final int BATCH_SIZE = 15000 ;
    
    private final ConcurrentHashMap<String, Message17> messageCache;
    private final BlockingQueue<Message17> messageQueue;
    private final ExecutorService processingExecutor;
    
    @Autowired
    private StationaryHealthInfo stationaryHealthInfo;
    
    public MessageService() {
        this.messageCache = new ConcurrentHashMap<>(); 
        this.messageQueue = new LinkedBlockingQueue<>(BATCH_SIZE * 2);
        this.processingExecutor = Executors.newFixedThreadPool(
            Runtime.getRuntime().availableProcessors(),
            r -> {
                Thread t = new Thread(r);
                t.setPriority(Thread.MAX_PRIORITY);
                return t;
            }
        );
        startMessageProcessor();
    }

    @Transactional
    public void decodeHexData(byte[] data) {
        int offset = 0;
        while (offset < data.length) {
            try {
                
                offset += 2; 
                String messageTypeHex = CommonFile.bytesToHex(data, offset, 1);
                offset += 1;
                List<Message17> tempBatch = new ArrayList<>();
                switch (messageTypeHex) {
                    case "17":
                        offset = stationaryHealthInfo.decodeMessageType17(data, offset, offset, tempBatch);
                        for (Message17 message : tempBatch) {
                            messageQueue.offer(message);
                        }
                        break;
                }
            } catch (Exception e) {
                ;
                offset++;
            }
        }
    }

    private void startMessageProcessor() {
        int threadCount = Runtime.getRuntime().availableProcessors();
        for (int i = 0; i < threadCount; i++) {
            processingExecutor.submit(this::processMessageBatch);
        }
    }

    private void processMessageBatch() {
        List<Message17> batch = new ArrayList<>(BATCH_SIZE);
        
        while (!Thread.currentThread().isInterrupted()) {
            try {
                Message17 message = messageQueue.poll(50, TimeUnit.MILLISECONDS);
                if (message != null) {
                    batch.add(message);
                    messageQueue.drainTo(batch, BATCH_SIZE - batch.size());
                    
                    if (!batch.isEmpty()) {
                        for (Message17 msg : batch) {
                            String key = generateKey(msg);
                            messageCache.put(key, msg);
                        }
                        batch.clear();
                    }
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }

    private String generateKey(Message17 message) {
        return message.getMessageSequence() + "_" + 
               message.getMessageDate() + "_" + 
               message.getMessageTime();
    }

    @PreDestroy
    public void shutdown() {
        processingExecutor.shutdown();
        try {
            if (!processingExecutor.awaitTermination(5, TimeUnit.SECONDS)) {
                processingExecutor.shutdownNow();
            }
        } catch (InterruptedException e) {
            processingExecutor.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }
    public int getCacheSize() {
        return messageCache.size();
    }
}