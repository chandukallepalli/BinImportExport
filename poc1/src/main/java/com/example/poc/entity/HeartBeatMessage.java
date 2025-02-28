package com.example.poc.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;


@Getter
@Setter
public class HeartBeatMessage {

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

    @Column(name = "primary_secondary_s_kavach")
    private int primarySecondarySKavach;

    @Column(name = "secondary_secondary_s_kavach")
    private int secondarySecondarySKavach;

    @Column(name = "packet_message_length")
    private int packetMessageLength;

    @Column(name = "frame_number")
    private int frameNumber;

    @Column(name = "packet_message_sequence")
    private int packetMessageSequence;

    @Column(name = "mac_code", length = 255)
    private String macCode;

    @Column(name = "created_at")
    private LocalDateTime createdAt;
}
