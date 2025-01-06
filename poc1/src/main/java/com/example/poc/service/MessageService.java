package com.example.poc.service;

import com.example.poc.entity.*;
import com.example.poc.util.AdjecentKavachBatch;
import com.example.poc.util.CommonFile;
import com.example.poc.util.StationaryHealthInfo;
import com.fasterxml.jackson.core.JsonProcessingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jakarta.annotation.PreDestroy;
import jakarta.transaction.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;

@Service
public class MessageService {
    private static final int BATCH_SIZE = 5000; // Reduced batch size for better throughput
    private static final int QUEUE_CAPACITY = 10000;
    private static final int THREAD_POOL_SIZE = Runtime.getRuntime().availableProcessors() * 2;
    
    // Cache Maps with initial capacity and load factor
    private final ConcurrentHashMap<String, Message17> messageCache;
    private final ConcurrentHashMap<String, PdiVersionCheckPskToSsk> pdiVersionCheckPskToSsk;
    private final ConcurrentHashMap<String, PDIVersionCheckSskToPsk> pdiVersionCheckSskToPsk;
    private final ConcurrentHashMap<String, HeartBeatMessage> heartBeatMessage;
    private final ConcurrentHashMap<String, TrainHandoverRequest> trainHandoverRequest;
    private final ConcurrentHashMap<String, TrainTakenOver> trainTakenOver;
    private final ConcurrentHashMap<String, TrainHandoverCancellation> trainHandoverCancellation;
    private final ConcurrentHashMap<String, TSLRequest> tslRequest;
    private final ConcurrentHashMap<String, TrainHandoverCancellationAcknowledgement> trainHandoverCancellationAcknowledgement;
    private final ConcurrentHashMap<String, FieldElementsStatus> fieldElementsStatus;
    private final ConcurrentHashMap<String, FieldElementsStatusRequest> fieldElementsStatusRequest;
    private final ConcurrentHashMap<String, TSLAuthority> tslAuthority;
    private final ConcurrentHashMap<String, TrainRRi> trainRRi;

    // Message Queues with optimized capacity
    private final BlockingQueue<Message17> messageQueue;
    private final BlockingQueue<PdiVersionCheckPskToSsk> pdiVersionCheckPskToSskQueue;
    private final BlockingQueue<PDIVersionCheckSskToPsk> pdiVersionCheckSskToPskQueue;
    private final BlockingQueue<HeartBeatMessage> heartBeatMessageQueue;
    private final BlockingQueue<TrainHandoverRequest> trainHandoverRequestQueue;
    private final BlockingQueue<TrainTakenOver> trainTakenOverQueue;
    private final BlockingQueue<TrainHandoverCancellation> trainHandoverCancellationQueue;
    private final BlockingQueue<TSLRequest> tslRequestQueue;
    private final BlockingQueue<TrainHandoverCancellationAcknowledgement> trainHandoverCancellationAcknowledgementQueue;
    private final BlockingQueue<FieldElementsStatus> fieldElementsStatusQueue;
    private final BlockingQueue<FieldElementsStatusRequest> fieldElementsStatusRequestQueue;
    private final BlockingQueue<TSLAuthority> tslAuthorityQueue;
    private final BlockingQueue<TrainRRi> trainRRiQueue;

    private final ExecutorService processingExecutor;

    @Autowired
    private StationaryHealthInfo stationaryHealthInfo;
    
    @Autowired
    private AdjecentKavachBatch adjecentKavachBatch;

    public MessageService() {
        int initialCapacity = 10000;
        float loadFactor = 0.75f;
        this.messageCache = new ConcurrentHashMap<>(initialCapacity, loadFactor);
        this.pdiVersionCheckPskToSsk = new ConcurrentHashMap<>(initialCapacity, loadFactor);
        this.pdiVersionCheckSskToPsk = new ConcurrentHashMap<>(initialCapacity, loadFactor);
        this.heartBeatMessage = new ConcurrentHashMap<>(initialCapacity, loadFactor);
        this.trainHandoverRequest = new ConcurrentHashMap<>(initialCapacity, loadFactor);
        this.trainTakenOver = new ConcurrentHashMap<>(initialCapacity, loadFactor);
        this.trainHandoverCancellation = new ConcurrentHashMap<>(initialCapacity, loadFactor);
        this.tslRequest = new ConcurrentHashMap<>(initialCapacity, loadFactor);
        this.trainHandoverCancellationAcknowledgement = new ConcurrentHashMap<>(initialCapacity, loadFactor);
        this.fieldElementsStatus = new ConcurrentHashMap<>(initialCapacity, loadFactor);
        this.fieldElementsStatusRequest = new ConcurrentHashMap<>(initialCapacity, loadFactor);
        this.tslAuthority = new ConcurrentHashMap<>(initialCapacity, loadFactor);
        this.trainRRi = new ConcurrentHashMap<>(initialCapacity, loadFactor);
        this.messageQueue = new LinkedBlockingQueue<>(QUEUE_CAPACITY);
        this.pdiVersionCheckPskToSskQueue = new LinkedBlockingQueue<>(QUEUE_CAPACITY);
        this.pdiVersionCheckSskToPskQueue = new LinkedBlockingQueue<>(QUEUE_CAPACITY);
        this.heartBeatMessageQueue = new LinkedBlockingQueue<>(QUEUE_CAPACITY);
        this.trainHandoverRequestQueue = new LinkedBlockingQueue<>(QUEUE_CAPACITY);
        this.trainTakenOverQueue = new LinkedBlockingQueue<>(QUEUE_CAPACITY);
        this.trainHandoverCancellationQueue = new LinkedBlockingQueue<>(QUEUE_CAPACITY);
        this.tslRequestQueue = new LinkedBlockingQueue<>(QUEUE_CAPACITY);
        this.trainHandoverCancellationAcknowledgementQueue = new LinkedBlockingQueue<>(QUEUE_CAPACITY);
        this.fieldElementsStatusQueue = new LinkedBlockingQueue<>(QUEUE_CAPACITY);
        this.fieldElementsStatusRequestQueue = new LinkedBlockingQueue<>(QUEUE_CAPACITY);
        this.tslAuthorityQueue = new LinkedBlockingQueue<>(QUEUE_CAPACITY);
        this.trainRRiQueue = new LinkedBlockingQueue<>(QUEUE_CAPACITY);
        this.processingExecutor = new ThreadPoolExecutor(
            THREAD_POOL_SIZE,
            THREAD_POOL_SIZE * 2,
            60L, TimeUnit.SECONDS,
            new LinkedBlockingQueue<>(QUEUE_CAPACITY),
            r -> {
                Thread t = new Thread(r);
                t.setPriority(Thread.MAX_PRIORITY);
                return t;
            },
            new ThreadPoolExecutor.CallerRunsPolicy()
        );   
        startMessageProcessors();
    }

    private void startMessageProcessors() {
        for (int i = 0; i < THREAD_POOL_SIZE; i++) {
            processingExecutor.submit(this::processMessageBatch);
            processingExecutor.submit(this::processType14MessageBatches);
        }
    }

    private void processMessageBatch() {
        List<Message17> batch = new ArrayList<>(BATCH_SIZE);
        
        while (!Thread.currentThread().isInterrupted()) {
            try {
                Message17 message = messageQueue.poll(10, TimeUnit.MILLISECONDS);
                if (message != null) {
                    batch.add(message);
                    messageQueue.drainTo(batch, BATCH_SIZE - batch.size());
                    
                    if (!batch.isEmpty()) {
                        processBatchParallel(batch);
                        batch.clear();
                    }
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }

    private void processBatchParallel(List<Message17> batch) {
        batch.parallelStream().forEach(message -> {
            String key = generateKeyForMessage(message);
            if (key != null) {
                messageCache.put(key, message);
            }
        });
    }

    private void processType14MessageBatches() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                processQueueBatchParallel(pdiVersionCheckPskToSskQueue, pdiVersionCheckPskToSsk);
                processQueueBatchParallel(pdiVersionCheckSskToPskQueue, pdiVersionCheckSskToPsk);
                processQueueBatchParallel(heartBeatMessageQueue, heartBeatMessage);
                processQueueBatchParallel(trainHandoverRequestQueue, trainHandoverRequest);
                processQueueBatchParallel(trainTakenOverQueue, trainTakenOver);
                processQueueBatchParallel(trainHandoverCancellationQueue, trainHandoverCancellation);
                processQueueBatchParallel(tslRequestQueue, tslRequest);
                processQueueBatchParallel(trainHandoverCancellationAcknowledgementQueue, trainHandoverCancellationAcknowledgement);
                processQueueBatchParallel(fieldElementsStatusQueue, fieldElementsStatus);
                processQueueBatchParallel(fieldElementsStatusRequestQueue, fieldElementsStatusRequest);
                processQueueBatchParallel(tslAuthorityQueue, tslAuthority);
                processQueueBatchParallel(trainRRiQueue, trainRRi);
                
                Thread.sleep(10); 
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }

    private <T> void processQueueBatchParallel(BlockingQueue<T> queue, ConcurrentHashMap<String, T> cache) {
        List<T> batch = new ArrayList<>(BATCH_SIZE);
        queue.drainTo(batch, BATCH_SIZE);
        
        if (!batch.isEmpty()) {
            batch.parallelStream().forEach(message -> {
                String key = generateKeyForMessage(message);
                if (key != null) {
                    cache.put(key, message);
                }
            });
        }
    }

    @Transactional
    public void decodeHexData(byte[] data) {
        CompletableFuture.runAsync(() -> {
            int offset = 0;
            while (offset < data.length) {
                try {
                    offset += 2;
                    String messageTypeHex = CommonFile.bytesToHex(data, offset, 1);
                    offset += 1;

                    switch (messageTypeHex) {
                        case "14":
                            offset = processType14Message(data, offset);
                            break;
                        case "17":
                            offset = processType17Message(data, offset);
                            break;
                        default:
                            offset++;
                    }
                } catch (Exception e) {
                    offset++;
                }
            }
        }, processingExecutor);
    }
    private int processType14Message(byte[] data, int offset) throws JsonProcessingException {
        List<PdiVersionCheckPskToSsk> pdiVersionCheckPskToSskBatch = new ArrayList<>();
        List<PDIVersionCheckSskToPsk> pdiVersionCheckSskToPskBatch = new ArrayList<>();
        List<HeartBeatMessage> heartBeatMessageBatch = new ArrayList<>();
        List<TrainHandoverRequest> trainHandoverRequestBatch = new ArrayList<>();
        List<TrainTakenOver> trainTakenOverBatch = new ArrayList<>();
        List<TrainHandoverCancellation> trainHandoverCancellationBatch = new ArrayList<>();
        List<TSLRequest> tslRequestBatch = new ArrayList<>();
        List<TrainHandoverCancellationAcknowledgement> trainHandoverCancellationAcknowledgementBatch = new ArrayList<>();
        List<FieldElementsStatus> fieldElementsStatusBatch = new ArrayList<>(); 
        List<FieldElementsStatusRequest> fieldElementsStatusRequestBatch = new ArrayList<>();
        List<TSLAuthority> tslAuthorityBatch = new ArrayList<>();
        List<TrainRRi> trainRRiBatch = new ArrayList<>();
        int newOffset = adjecentKavachBatch.decodeMessageType14(data, offset, offset,
            pdiVersionCheckPskToSskBatch, pdiVersionCheckSskToPskBatch, 
            heartBeatMessageBatch, trainHandoverRequestBatch,
            trainTakenOverBatch, trainHandoverCancellationBatch,
            tslRequestBatch, trainHandoverCancellationAcknowledgementBatch,
            fieldElementsStatusBatch, fieldElementsStatusRequestBatch,
            tslAuthorityBatch, trainRRiBatch);
        CompletableFuture.allOf(
            CompletableFuture.runAsync(() -> addToQueue(pdiVersionCheckPskToSskBatch, pdiVersionCheckPskToSskQueue)),
            CompletableFuture.runAsync(() -> addToQueue(pdiVersionCheckSskToPskBatch, pdiVersionCheckSskToPskQueue)),
            CompletableFuture.runAsync(() -> addToQueue(heartBeatMessageBatch, heartBeatMessageQueue)),
            CompletableFuture.runAsync(() -> addToQueue(trainHandoverRequestBatch, trainHandoverRequestQueue)),
            CompletableFuture.runAsync(() -> addToQueue(trainTakenOverBatch, trainTakenOverQueue)),
            CompletableFuture.runAsync(() -> addToQueue(trainHandoverCancellationBatch, trainHandoverCancellationQueue)),
            CompletableFuture.runAsync(() -> addToQueue(fieldElementsStatusBatch, fieldElementsStatusQueue)),
            CompletableFuture.runAsync(() -> addToQueue(fieldElementsStatusRequestBatch, fieldElementsStatusRequestQueue)),
            CompletableFuture.runAsync(() -> addToQueue(tslAuthorityBatch, tslAuthorityQueue)),
            CompletableFuture.runAsync(() -> addToQueue(trainRRiBatch, trainRRiQueue))
        ).join();

        return newOffset;
    }

    private int processType17Message(byte[] data, int offset) {
        List<Message17> tempBatch = new ArrayList<>();
        int newOffset = stationaryHealthInfo.decodeMessageType17(data, offset, offset, tempBatch);
        addToQueue(tempBatch, messageQueue);
        return newOffset;
    }

    private <T> void addToQueue(List<T> batch, BlockingQueue<T> queue) {
        batch.forEach(message -> {
            try {
                if (!queue.offer(message, 100, TimeUnit.MILLISECONDS)) {
                    processSingleMessage(message);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });
    }

    private <T> void processSingleMessage(T message) {
        String key = generateKeyForMessage(message);
        if (key != null) {
            if (message instanceof Message17) {
                messageCache.put(key, (Message17) message);
            } else if (message instanceof PdiVersionCheckPskToSsk) {
                pdiVersionCheckPskToSsk.put(key, (PdiVersionCheckPskToSsk) message);
            } else if (message instanceof PDIVersionCheckSskToPsk) {
                pdiVersionCheckSskToPsk.put(key, (PDIVersionCheckSskToPsk) message);
            } else if (message instanceof HeartBeatMessage) {
                heartBeatMessage.put(key, (HeartBeatMessage) message);
            } else if (message instanceof TrainHandoverRequest) {
                trainHandoverRequest.put(key, (TrainHandoverRequest) message);
            } else if (message instanceof TrainTakenOver) {
                trainTakenOver.put(key, (TrainTakenOver) message);
            } else if (message instanceof TrainHandoverCancellation) {
                trainHandoverCancellation.put(key, (TrainHandoverCancellation) message);
            } else if (message instanceof TSLRequest) {
                tslRequest.put(key, (TSLRequest) message);
            } else if (message instanceof TrainHandoverCancellationAcknowledgement) {
                trainHandoverCancellationAcknowledgement.put(key, (TrainHandoverCancellationAcknowledgement) message);
            } else if (message instanceof FieldElementsStatus) {
                fieldElementsStatus.put(key, (FieldElementsStatus) message);
            } else if (message instanceof FieldElementsStatusRequest) {
                fieldElementsStatusRequest.put(key, (FieldElementsStatusRequest) message);
            } else if (message instanceof TSLAuthority) {
                tslAuthority.put(key, (TSLAuthority) message);
            } else if (message instanceof TrainRRi) {
                trainRRi.put(key, (TrainRRi) message);
            } else {
                System.err.println("Unsupported message type: " + message.getClass().getName());
            }
        }
    }

    private <T> String generateKeyForMessage(T message) {
        try {
            if (message instanceof Message17) {
                Message17 msg = (Message17) message;
                return msg.getMessageSequence() + "_" + msg.getMessageDate() + "_" + msg.getMessageTime();
            } else if (message instanceof PdiVersionCheckPskToSsk) {
                PdiVersionCheckPskToSsk msg = (PdiVersionCheckPskToSsk) message;
                return msg.getDate() + "_" + msg.getTime();
            } else if (message instanceof PDIVersionCheckSskToPsk) {
                PDIVersionCheckSskToPsk msg = (PDIVersionCheckSskToPsk) message;
                return msg.getDate() + "_" + msg.getTime();
            } else if (message instanceof HeartBeatMessage) {
                HeartBeatMessage msg = (HeartBeatMessage) message;
                return msg.getDate() + "_" + msg.getTime();
            } else if (message instanceof TrainHandoverRequest) {
                TrainHandoverRequest msg = (TrainHandoverRequest) message;
                return msg.getDate() + "_" + msg.getTime();
            } else if (message instanceof TrainTakenOver) {
                TrainTakenOver msg = (TrainTakenOver) message;
                return msg.getDate() + "_" + msg.getTime();
            } else if (message instanceof TrainHandoverCancellation) {
                TrainHandoverCancellation msg = (TrainHandoverCancellation) message;
                return msg.getDate() + "_" + msg.getTime();
            } else if (message instanceof TSLRequest) {
                TSLRequest msg = (TSLRequest) message;
                return msg.getDate() + "_" + msg.getTime();
            } else if (message instanceof TrainHandoverCancellationAcknowledgement) {
                TrainHandoverCancellationAcknowledgement msg = (TrainHandoverCancellationAcknowledgement) message;
                return msg.getDate() + "_" + msg.getTime();
            } else if (message instanceof FieldElementsStatus) {
                FieldElementsStatus msg = (FieldElementsStatus) message;
                return msg.getDate() + "_" + msg.getTime();
            } else if (message instanceof FieldElementsStatusRequest) {
                FieldElementsStatusRequest msg = (FieldElementsStatusRequest) message;
                return msg.getDate() + "_" + msg.getTime();
            } else if (message instanceof TSLAuthority) {
                TSLAuthority msg = (TSLAuthority) message;
                return msg.getDate() + "_" + msg.getTime();
            } else if (message instanceof TrainRRi) {
                TrainRRi msg = (TrainRRi) message;
                return msg.getDate() + "_" + msg.getTime();
            } else {
                
                System.err.println("Unsupported message type for key generation: " + message.getClass().getName());
                return null;
            }
        } catch (Exception e) {
            System.err.println("Error generating key for message: " + e.getMessage());
            return null;
        }
    }

    public Map<String, Object> getMessagesByDateRangeAndFilters(String fromDate, String toDate, 
    int page, int pageSize, Map<String, List<Integer>> filters) {
    
    try {
        
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date startDate = dateFormat.parse(fromDate);
        Date endDate = dateFormat.parse(toDate);

        
        List<Message17> filteredMessages = messageCache.values().parallelStream()
            .filter(msg -> {
                try {
                    
                    String msgDateTime = msg.getMessageDate() + " " + msg.getMessageTime();
                    Date msgDate = dateFormat.parse(msgDateTime);
                    boolean withinDateRange = !msgDate.before(startDate) && !msgDate.after(endDate);
                    boolean matchesFilters = true;
                    if (filters != null) {
                        if (filters.containsKey("stationaryKavachId") && !filters.get("stationaryKavachId").isEmpty()) {
                            matchesFilters &= filters.get("stationaryKavachId")
                                .contains(msg.getStationaryKavachId());
                        }
                        if (filters.containsKey("messageLength") && !filters.get("messageLength").isEmpty()) {
                            matchesFilters &= filters.get("messageLength")
                                .contains(msg.getMessageLength());
                        }
                        if (filters.containsKey("messageSequence") && !filters.get("messageSequence").isEmpty()) {
                            matchesFilters &= filters.get("messageSequence")
                                .contains(msg.getMessageSequence());
                        }
                    }
                    
                    return withinDateRange && matchesFilters;       
                } catch (ParseException e) {
                    return false;
                }
            })
            .sorted((m1, m2) -> {
                try {
                    Date date1 = dateFormat.parse(m1.getMessageDate() + " " + m1.getMessageTime());
                    Date date2 = dateFormat.parse(m2.getMessageDate() + " " + m2.getMessageTime());
                    return date2.compareTo(date1); 
                } catch (ParseException e) {
                    return 0;
                }
            })
            .collect(Collectors.toList());
        int totalElements = filteredMessages.size();
        int totalPages = (int) Math.ceil((double) totalElements / pageSize);
        int startIndex = page * pageSize;
        int endIndex = Math.min(startIndex + pageSize, totalElements);
        List<Message17> paginatedMessages = startIndex < endIndex 
            ? filteredMessages.subList(startIndex, endIndex)
            : new ArrayList<>();
        Map<String, Object> response = new HashMap<>();
        response.put("content", paginatedMessages);
        response.put("totalElements", totalElements);
        response.put("totalPages", totalPages);
        response.put("currentPage", page);
        response.put("pageSize", pageSize);
        response.put("hasNext", page < totalPages - 1);
        response.put("hasPrevious", page > 0);

        return response;
        
    } catch (ParseException e) {
        throw new IllegalArgumentException("Invalid date format. Expected format: yyyy-MM-dd HH:mm:ss");
    }
}
    
    
}