package com.example.poc.dto;

import java.time.LocalDateTime;
import java.util.List;

import com.example.poc.entity.Message17;

public class Message17Dto {

    private Long id;
    private int messageLength;
    private int messageSequence;
    private int stationaryKavachId;
    private int nmsSystemId;
    private int systemVersion;
    private String messageDate;
    private String messageTime;
    private String firmName;
    private String eventInfo;
    private String packetName;
    private String eventDescription;
    private int eventCount;
    private String eventDetails;
    private String crc;
    private LocalDateTime createdAt;

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getMessageLength() {
        return messageLength;
    }

    public void setMessageLength(int messageLength) {
        this.messageLength = messageLength;
    }

    public int getMessageSequence() {
        return messageSequence;
    }

    public void setMessageSequence(int messageSequence) {
        this.messageSequence = messageSequence;
    }

    public int getStationaryKavachId() {
        return stationaryKavachId;
    }

    public void setStationaryKavachId(int stationaryKavachId) {
        this.stationaryKavachId = stationaryKavachId;
    }

    public int getNmsSystemId() {
        return nmsSystemId;
    }

    public void setNmsSystemId(int nmsSystemId) {
        this.nmsSystemId = nmsSystemId;
    }

    public int getSystemVersion() {
        return systemVersion;
    }

    public void setSystemVersion(int systemVersion) {
        this.systemVersion = systemVersion;
    }

    public String getMessageDate() {
        return messageDate;
    }

    public void setMessageDate(String messageDate) {
        this.messageDate = messageDate;
    }

    public String getMessageTime() {
        return messageTime;
    }

    public void setMessageTime(String messageTime) {
        this.messageTime = messageTime;
    }

    public String getFirmName() {
        return firmName;
    }

    public void setFirmName(String firmName) {
        this.firmName = firmName;
    }

    public String getEventInfo() {
        return eventInfo;
    }

    public void setEventInfo(String eventInfo) {
        this.eventInfo = eventInfo;
    }

    public String getPacketName() {
        return packetName;
    }

    public void setPacketName(String packetName) {
        this.packetName = packetName;
    }

    public String getEventDescription() {
        return eventDescription;
    }

    public void setEventDescription(String eventDescription) {
        this.eventDescription = eventDescription;
    }

    public int getEventCount() {
        return eventCount;
    }

    public void setEventCount(int eventCount) {
        this.eventCount = eventCount;
    }

    public String getEventDetails() {
        return eventDetails;
    }

    public void setEventDetails(String eventDetails) {
        this.eventDetails = eventDetails;
    }

    public String getCrc() {
        return crc;
    }

    public void setCrc(String crc) {
        this.crc = crc;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    // Constructor
    public Message17Dto(Long id, int messageLength, int messageSequence, int stationaryKavachId, int nmsSystemId,
                        int systemVersion, String messageDate, String messageTime, String firmName, String eventInfo,
                        String packetName, String eventDescription, int eventCount, String eventDetails, String crc,
                        LocalDateTime createdAt) {
        this.id = id;
        this.messageLength = messageLength;
        this.messageSequence = messageSequence;
        this.stationaryKavachId = stationaryKavachId;
        this.nmsSystemId = nmsSystemId;
        this.systemVersion = systemVersion;
        this.messageDate = messageDate;
        this.messageTime = messageTime;
        this.firmName = firmName;
        this.eventInfo = eventInfo;
        this.packetName = packetName;
        this.eventDescription = eventDescription;
        this.eventCount = eventCount;
        this.eventDetails = eventDetails;
        this.crc = crc;
        this.createdAt = createdAt;
    }

    public List<Message17Dto> Message17Dto(List<Message17Dto> data) {
        return data;
    }

    // public Message17Dto(List<Message17> data, LocalDateTime now) {

    //     //TODO Auto-generated constructor stub
    // }

    // public record StoredStudentData(Object data, LocalDateTime createdTime) {
    //     boolean isExpired(LocalDateTime currentTime) {
    //         return createdTime.plusDays(2).isBefore(currentTime);
    //     }
    // }

    // public List<Message17> Message17Dto(List<Message17> data) {
    //     return data;
    //     //TODO Auto-generated constructor stub
    // }

    // public Message17Dto(List<Message17> data, LocalDateTime now) {
    //     //TODO Auto-generated constructor stub
    // }
}