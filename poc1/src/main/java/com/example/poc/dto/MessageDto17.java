package com.example.poc.dto;

import java.time.LocalDateTime;
import java.util.List;

import com.example.poc.entity.Message17;

public class MessageDto17 {
    private List<Message17Dto> messages;
    private LocalDateTime storedAt;

    public MessageDto17(List<Message17Dto> data, LocalDateTime storedAt) {
        this.messages = data;
        this.storedAt = storedAt;
    }

    // Getters and Setters
    public List<Message17Dto> getMessages() {
        return messages;
    }

    public void setMessages(List<Message17Dto> messages) {
        this.messages = messages;
    }

    public LocalDateTime getStoredAt() {
        return storedAt;
    }

    public void setStoredAt(LocalDateTime storedAt) {
        this.storedAt = storedAt;
    }
}

