package com.example.poc.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;


@Getter
@Setter
public class PDIVersionCheckSskToPsk {

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

    @Column(name = "packet_length")
    private int packetLength;

    @Column(name = "result_pdi_version_ckeck")
    private int resultPdiVersionCheck;

    @Column(name = "pdi_version_ssk")
    private int pdiVersionSsk;

    @Column(name = "rN")
    private int rN;

    @Column(name = "mac_code")
    private String macCode;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

}

