package com.example.inmemory.cleanup;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.example.inmemory.Storage.InMemoryStudentStore;

@Component
public class DataCleanupTask {
    private final InMemoryStudentStore studentStore;

    public DataCleanupTask(InMemoryStudentStore studentStore) {
        this.studentStore = studentStore;
    }

    @Scheduled(fixedRate = 3600000) // Runs every hour
    public void cleanUpExpiredData() {
        studentStore.cleanUpExpiredData();
    }
}

