package com.example.poc.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;


@Getter
@Setter
public class TrainHandoverRequest {

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

    @Column(name = "sender_identifier", nullable = false)
    private int senderIdentifier;

    @Column(name = "receiver_identifier", nullable = false)
    private int receiverIdentifier;

    @Column(name = "packet_message_length", nullable = false)
    private int packetMessageLength;

    @Column(name = "frame_number", nullable = false)
    private int frameNumber;

    @Column(name = "packet_message_sequence", nullable = false)
    private int packetMessageSequence;

    @Column(name = "border_rfid_tag", nullable = false)
    private int borderRfidTag;

    @Column(name = "in_sections", nullable = false)
    private String inSections;

    @Column(name = "dist_to_border_tag_location", nullable = false)
    private int distToBorderTagLocation;

    @Column(name = "onboard_packet_name")
    private String onboardPacketName;

    @Column(name = "on_packet_length")
    private int onPacketLength;

    @Column(name = "on_frame_number")
    private int onFrameNumber;

    @Column(name = "source_loco_id")
    private int sourceLocoId;

    @Column(name = "source_loco_version")
    private int sourceLocoVersion;

    @Column(name = "abs_loco_loc")
    private int absLocoLoc;

    @Column(name = "l_doubt_over")
    private int lDoubtOver;

    @Column(name = "l_doubt_under")
    private int lDoubtUnder;

    @Column(name = "train_int")
    private String trainInt;

    @Column(name = "train_length")
    private int trainLength;

    @Column(name = "train_speed")
    private int trainSpeed;

    @Column(name = "movement_dir")
    private String movementDir;

    @Column(name = "emergency_status")
    private String emergencyStatus;

    @Column(name = "loco_mode")
    private String locoMode;

    @Column(name = "last_rfid_tag")
    private int lastRfidTag;

    @Column(name = "tag_dup")
    private String tagDup;

    @Column(name = "tag_link_info")
    private String tagLinkInfo;

    @Column(name = "tin")
    private int tin;

    @Column(name = "brake_applied")
    private int breakApplied;

    @Column(name = "new_ma_reply")
    private int newMaReply;

    @Column(name = "last_ref_profile_num")
    private int lastRefProfileNum;

    @Column(name = "sig_ov")
    private String sigOv;

    @Column(name = "info_ack")
    private int infoAck;

    @Column(name = "spare")
    private int spare;

    @Column(name = "loco_health_status")
    private int locoHealthStatus;
    @Column(name ="fault_msg")
    private String faultMsg;

    @Column(name = "mac_code", nullable = false, length = 255)
    private String macCode;

    @Column(name = "created_at")
    private LocalDateTime createdAt;
   
}

