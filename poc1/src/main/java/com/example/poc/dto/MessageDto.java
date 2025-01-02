package com.example.poc.dto;

public class MessageDto {
    private Integer id;
    private String crc;
    private String createdAt;
    private Integer faultCode;
    private String faultCodeType;
    private String faultMessage;
    private String firmName;
    private int kavachSubSystemId;
    private String kavachSubSystemType;
    private String messageDate;
    private Integer messageLength;
    private Integer messageSequence;
    private String messageTime;
    private int moduleId;
    private int nmsSystemId;
    private int systemVersion;
    private Integer totalFaultCodes;

    // Getters and Setters
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

    public Integer getMessageLength() {
        return messageLength;
    }

    public void setMessageLength(Integer messageLength) {
        this.messageLength = messageLength;
    }

    public Integer getMessageSequence() {
        return messageSequence;
    }

    public void setMessageSequence(Integer messageSequence) {
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

    public Integer getTotalFaultCodes() {
        return totalFaultCodes;
    }

    public void setTotalFaultCodes(Integer totalFaultCodes) {
        this.totalFaultCodes = totalFaultCodes;
    }
}
