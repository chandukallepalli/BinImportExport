package com.example.poc.entity;

import java.time.LocalDateTime;

import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvBindByPosition;

public class Message {
    @CsvBindByPosition(position = 0)
    private Integer id;

    @CsvBindByPosition(position = 1)
    private String crc;

    @CsvBindByPosition(position = 2)
    private String createdAt;

    @CsvBindByPosition(position = 3)
    private Integer faultCode;

    @CsvBindByPosition(position = 4)
    private String faultCodeType;

    @CsvBindByPosition(position = 5)
    private String faultMessage;

    @CsvBindByPosition(position = 6)
    private String firmName;

    @CsvBindByPosition(position = 7)
    private int kavachSubSystemId;

    @CsvBindByPosition(position = 8)
    private String kavachSubSystemType;

    @CsvBindByPosition(position = 9)
    private String messageDate;

    @CsvBindByPosition(position = 10)
    private Integer messageLength;

    @CsvBindByPosition(position = 11)
    private Integer messageSequence;

    @CsvBindByPosition(position = 12)
    private String messageTime;

    @CsvBindByPosition(position = 13)
    private int moduleId;

    @CsvBindByPosition(position = 14)
    private int nmsSystemId;

    @CsvBindByPosition(position = 15)
    private int systemVersion;

    @CsvBindByPosition(position = 16)
    private Integer totalFaultCodes;

    public Message(Object data, LocalDateTime timestamp) {
        //TODO Auto-generated constructor stub
    }
    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public String getCrc() {
        return crc;
    }
    public void setCrc(String crc) {
        this.crc = crc;
    }
    public String getCreatedAt() {
        return createdAt;
    }
    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
    public Integer getFaultCode() {
        return faultCode;
    }
    public void setFaultCode(Integer faultCode) {
        this.faultCode = faultCode;
    }
    public String getFaultCodeType() {
        return faultCodeType;
    }
    public void setFaultCodeType(String faultCodeType) {
        this.faultCodeType = faultCodeType;
    }
    public String getFaultMessage() {
        return faultMessage;
    }
    public void setFaultMessage(String faultMessage) {
        this.faultMessage = faultMessage;
    }
    public String getFirmName() {
        return firmName;
    }
    public void setFirmName(String firmName) {
        this.firmName = firmName;
    }
    public int getKavachSubSystemId() {
        return kavachSubSystemId;
    }
    public void setKavachSubSystemId(int kavachSubSystemId) {
        this.kavachSubSystemId = kavachSubSystemId;
    }
    public String getKavachSubSystemType() {
        return kavachSubSystemType;
    }
    public void setKavachSubSystemType(String kavachSubSystemType) {
        this.kavachSubSystemType = kavachSubSystemType;
    }
    public String getMessageDate() {
        return messageDate;
    }
    public void setMessageDate(String messageDate) {
        this.messageDate = messageDate;
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
    public String getMessageTime() {
        return messageTime;
    }
    public void setMessageTime(String messageTime) {
        this.messageTime = messageTime;
    }
    public int getModuleId() {
        return moduleId;
    }
    public void setModuleId(int moduleId) {
        this.moduleId = moduleId;
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
    public int getTotalFaultCodes() {
        return totalFaultCodes;
    }
    public void setTotalFaultCodes(int totalFaultCodes) {
        this.totalFaultCodes = totalFaultCodes;
    }

   
    
}
