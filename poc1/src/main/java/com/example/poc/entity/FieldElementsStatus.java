package com.example.poc.entity;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Transient;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class FieldElementsStatus {
        @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) 
    private Long id;

    @Column(name = "message_length")
    private int messageLength;

    @Column(name = "message_sequence")
    private int messageSequence;

    @Column(name = "stationary_kavach_id")
    private int stationaryKavachId;

    @Column(name = "nms_system_id")
    private int nmsSystemId;

    @Column(name = "system_version")
    private int systemVersion;

    @Column(name = "message_date")
    private String date;

    @Column(name = "message_time")
    private String time;

    @Column(name = "crc")
    private String crc;

    @Column(name = "specific_protocol")
    private String specificProtocol;
   
    @Column(name = "packet_name")
    private String packetName;

    @Column(name = "sender_identifier")
    private int senderIdentifier;

    @Column(name = "receiver_identifier")
    private int receiverIdentifier;

    @Column(name = "packet_message_length")
    private int packetMessageLength;

    @Column(name = "frame_number")
    private int frameNumber;

    @Column(name = "packet_message_sequence")
    private int packetMessageSequence;

    @Column(name = "total_field_elements")
    private int totalFieldElements;

    @Column(name = "all_field_elements")
    private byte[] allFieldElements;
   
    @Transient
private List<String> allFieldElementsList; 
    public List<String> getAllFieldElementsList() {
        if (allFieldElements != null) {
            try (ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(allFieldElements))) {
                return (List<String>) ois.readObject(); 
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
   
    /**
     * Setter for the relay status list.
     * Serializes the list of strings into a byte array for storage.
     */
    public void setAllFieldElementsList(List<String> relayStatusList) {
        this.allFieldElementsList = relayStatusList; // Store the list in the transient field.
        if (relayStatusList != null) {
            try (ByteArrayOutputStream bos = new ByteArrayOutputStream();
                 ObjectOutputStream oos = new ObjectOutputStream(bos)) {
                oos.writeObject(relayStatusList); // Serialize the list into a byte array.
                this.allFieldElements = bos.toByteArray(); // Store the byte array in the database field.
            } catch (IOException e) {
                e.printStackTrace(); // Log the exception.
            }
        } else {
            this.allFieldElements = null; // Clear the byte array if the list is null.
        }
    }

    @Column(name = "mac_code")
    private String macCode;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

}

