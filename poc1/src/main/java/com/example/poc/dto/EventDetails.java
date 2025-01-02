package com.example.poc.dto;
public class EventDetails {
    private int eventId;
    private String eventName;
    private String eventDescription;
    private int eventSize;  // Added this new field


    // Constructor
    public EventDetails(int eventId, String eventName, String eventDescription) {
        this.eventId = eventId;
        this.eventName = eventName;
        this.eventDescription = eventDescription;
    }

    // Getters and setters
    public int getEventId() {
        return eventId;
    }

    public String getEventName() {
        return eventName;
    }

    public String getEventDescription() {
        return eventDescription;
    }

    public int getEventSize() {
        return eventSize;
    }
}