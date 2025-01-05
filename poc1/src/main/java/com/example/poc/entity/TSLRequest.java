package com.example.poc.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class TSLRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Assuming an auto-incrementing primary key
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

    @Column(name = "border_rfid_tag")
    private int borderRfidTag;

    @Column(name = "onboard_kavach_identity")
    private int onboardKavachIdentity;

    @Column(name = "tsl_route_id")
    private String tslRouteId;

    @Column(name = "mac_code")
    private String macCode;

    @Column(name = "created_at")
    private LocalDateTime createdAt;
}

