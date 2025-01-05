package com.example.poc.entity;



import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;


@Getter
@Setter
public class PdiVersionCheckPskToSsk {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Assuming an auto-incrementing primary key
    private Long id;

    @Column(name = "message_length", nullable = false)
    private int messageLength;

    @Column(name = "message_sequence", nullable = false)
    private int messageSequence;

    @Column(name = "stationary_kavach_id", nullable = false)
    private int stationaryKavachId;

    @Column(name = "nms_system_id", nullable = false)
    private int nmsSystemId;

    @Column(name = "system_version", nullable = false)
    private int systemVersion;

    @Column(name = "message_date", nullable = false)
    private String date;

    @Column(name = "message_time", nullable = false)
    private String time;

    @Column(name = "crc", nullable = false)
    private String crc;

    @Column(name = "packet_name")
    private String packetName;

    @Column(name = "sender_identifier", nullable = false)
    private int senderIdentifier;

    @Column(name = "receiver_identifier", nullable = false)
    private int receiverIdentifier;

    @Column(name = "packet_length", nullable = false)
    private int packetLength;

    @Column(name = "pdi_version", nullable = false)
    private int pdiVersion;

    @Column(name = "rp", nullable = false)
    private int rp;

    @Column(name = "specific_protocol", nullable = false)
    private String specificProtocol;

    @Column(name = "created_at")
    private LocalDateTime createdAt;
}


