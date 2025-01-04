package com.example.poc.service;


import com.example.poc.entity.Message17;
import com.example.poc.util.CommonFile;

import com.example.poc.util.StationaryHealthInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.text.ParseException;


import java.text.SimpleDateFormat;
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

    public List<Message17> getMessagesByDateRange(Date fromDate, Date toDate) {
        SimpleDateFormat cacheFormat = new SimpleDateFormat("dd/MM/yy HH:mm:ss");
        List<Message17> result = new ArrayList<>();
        
        // logger.info("Current cache size: {}", messageCache.size());
        // logger.info("Searching between {} and {}", cacheFormat.format(fromDate), cacheFormat.format(toDate));
        
        // Debug: Print first few cache entries
        int debugCount = 0;
        for (Map.Entry<String, Message17> entry : messageCache.entrySet()) {
            if (debugCount < 5) {
                // logger.debug("Cache entry key: {}", entry.getKey());
                // logger.debug("Cache entry date: {} {}", entry.getValue().getMessageDate(), 
                //             entry.getValue().getMessageTime());
                debugCount++;
            }
            
            Message17 message = entry.getValue();
            try {
                String messageDateTimeStr = message.getMessageDate() + " " + message.getMessageTime();
               // logger.debug("Checking message datetime: {}", messageDateTimeStr);
                
                // Convert the year format if needed (assuming message date is in yy format)
                if (message.getMessageDate().length() == 8) { // dd/MM/yy
                    String[] dateParts = message.getMessageDate().split("/");
                    if (dateParts.length == 3) {
                        // Adjust the year if needed
                        int year = Integer.parseInt(dateParts[2]);
                        if (year < 50) { // Assuming years 00-49 are 2000-2049
                            dateParts[2] = String.format("%02d", year);
                        }
                        messageDateTimeStr = String.format("%s/%s/%s %s", 
                            dateParts[0], dateParts[1], dateParts[2], message.getMessageTime());
                    }
                }
                
                Date messageDateTime = cacheFormat.parse(messageDateTimeStr);
                
                if (!messageDateTime.before(fromDate) && !messageDateTime.after(toDate)) {
                  //  logger.debug("Adding message to result, datetime: {}", messageDateTimeStr);
                    result.add(message);
                }
            } catch (ParseException e) {
             //   logger.error("Error parsing date for message: {} {}", 
             //               message.getMessageDate(), message.getMessageTime(), e);
                continue;
            }
        }
        
       // logger.info("Found {} messages in date range", result.size());
        
        // Sort results by date/time
        result.sort((m1, m2) -> {
            try {
                Date d1 = cacheFormat.parse(m1.getMessageDate() + " " + m1.getMessageTime());
                Date d2 = cacheFormat.parse(m2.getMessageDate() + " " + m2.getMessageTime());
                return d1.compareTo(d2);
            } catch (ParseException e) {
                //logger.error("Error during sorting", e);
                return 0;
            }
        });
        
        return result;
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
    public Message17 getRandomMessage() {
        if (messageCache.isEmpty()) {
            return null;
        }

        List<Message17> messages = new ArrayList<>(messageCache.values());
        return messages.get(new Random().nextInt(messages.size()));
    }
}