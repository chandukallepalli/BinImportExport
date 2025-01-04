package com.example.poc.dto;

public class MessageSearchRequest {
    private String fromDate;
    private String toDate;
    
    // Getters and setters
    public String getFromDate() {
        return fromDate;
    }
    
    public void setFromDate(String fromDate) {
        this.fromDate = fromDate;
    }
    
    public String getToDate() {
        return toDate;
    }
    
    public void setToDate(String toDate) {
        this.toDate = toDate;
    }
}

