package com.example.inmemory.Storage;



import java.time.LocalDateTime;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Component;
@Component
public class InMemoryStudentStore {
    private final ConcurrentHashMap<String, StoredStudentData> dataStore = new ConcurrentHashMap<>();

    public void storeData(String key, Object data) {
        dataStore.put(key, new StoredStudentData(data, LocalDateTime.now()));
    }

    public Object getData(String key) {
        return dataStore.get(key).data();
    }

    public ConcurrentHashMap<String, StoredStudentData> getAllData() {
        return dataStore;
    }

    public void cleanUpExpiredData() {
        LocalDateTime now = LocalDateTime.now();
        dataStore.entrySet().removeIf(entry -> entry.getValue().isExpired(now));
    }

    public record StoredStudentData(Object data, LocalDateTime createdTime) {
        boolean isExpired(LocalDateTime currentTime) {
            return createdTime.plusDays(2).isBefore(currentTime);
        }
    }
}
