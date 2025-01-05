package com.example.poc.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;


@Getter
@Setter
public class TrainRRi {
    
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

    @Column(name = "ref_profile_id")
    private int refProfileId;

    @Column(name = "onboard_kavach_identity")
    private int onboardKavachIdentity;

    @Column(name = "sub_pkt_type")
    private String subPktType;

    @Column(name = "sub_pkt_len_ma")
    private Integer subPktLenMA;

    @Column(name = "frame_offset")
    private String frameOffset;

    @Column(name = "dst_loco_sos")
    private String dstLocoSos;

    @Column(name = "train_section_type")
    private String trainSectionType;

    @Column(name = "line_number")
    private String lineNumber;

    @Column(name = "line_name")
    private String lineName;

    @Column(name = "type_of_signal")
    private String typeOfSignal;

    @Column(name = "signal_ov")
    private String signalOv;

    @Column(name = "stop_signal")
    private String stopSignal;

    @Column(name = "current_sig_aspect")
    private String currentSigAspect;

    @Column(name = "next_sig_aspect")
    private String nextSigAspect;

    @Column(name = "approaching_signal_distance")
    private Integer approachingSignalDistance;

    @Column(name = "authority_type")
    private String authorityType;

    @Column(name = "authorized_speed")
    private String authorizedSpeed;

    @Column(name = "ma_wrt_sig")
    private Integer maWRTSig;

    @Column(name = "req_shorten_ma")
    private String reqShortenMa;

    @Column(name = "new_ma")
    private String newMa;

    @Column(name = "train_length_info_sts")
    private String trainLengthInfoSts;

    @Column(name = "trn_len_info_type")
    private String trnLenInfoType;

    @Column(name = "ref_frame_num_tlm")
    private Integer refFrameNumTlm;

    @Column(name = "ref_offset_int_tlm")
    private Integer refOffsetIntTlm;

    @Column(name = "next_stn_comm")
    private String nextStnComm;

    @Column(name = "appr_stn_ilc_ibs_id")
    private Integer apprStnIlcIbsId;

    @Column(name = "sub_pkt_type_ssp")
    private String subPktTypeSSP;

    @Column(name = "sub_pkt_len_ssp")
    private Integer subPktLenSSP;

    @Column(name = "ssp_count_info")
    private Integer sspCountInfo;

    @Lob
    @Column(name = "classified_speed_info", columnDefinition = "TEXT")
    private String classifiedSpeedInfo; // JSON-serialized List<Map<String, Object>>

    @Column(name = "sub_pkt_type_gp")
    private String subPktTypeGP;

    @Column(name = "sub_pkt_len_gp")
    private Integer subPktLenGP;

    @Column(name = "gp_count_info")
    private Integer gpCountInfo;

    @Lob
    @Column(name = "gradient_info", columnDefinition = "TEXT")
    private String gradientInfo; // JSON-serialized List<Map<String, Object>>

    @Column(name = "sub_pkt_type_lc")
    private String subPktTypeLC;

    @Column(name = "sub_pkt_len_lc")
    private Integer subPktLenLC;

    @Column(name = "lm_count_info")
    private Integer lmCountInfo;

    @Lob
    @Column(name = "lc_info", columnDefinition = "TEXT")
    private String lcInfo; // JSON-serialized List<Map<String, Object>>

    @Column(name = "sub_pkt_type_tsp")
    private String subPktTypeTSP;

    @Column(name = "sub_pkt_len_tsp")
    private Integer subPktLenTSP;

    @Column(name = "to_count_info")
    private Integer toCountInfo;

    @Lob
    @Column(name = "speed_info", columnDefinition = "TEXT")
    private String speedInfo; // JSON-serialized List<Map<String, Object>>

    @Column(name = "sub_pkt_type_tag")
    private String subPktTypeTag;

    @Column(name = "sub_pkt_len_tag")
    private Integer subPktLenTag;

    @Column(name = "dist_dup_tag")
    private Integer distDupTag;

    @Column(name = "route_rfid_cnt")
    private Integer routeRfidCount;

    @Lob
    @Column(name = "rfid_info_list", columnDefinition = "TEXT")
    private String rfidInfoList;

    @Column(name = "abs_loc_reset")
    private String absLocReset;

    @Column(name = "start_dist_to_loc_reset")
    private Integer startDistToLocReset;

    @Column(name = "adj_loc_dir")
    private String adjLocoDir;

    @Column(name = "abs_loc_correction")
    private Integer absLocCorrection;

    @Column(name = "adj_line_cnt")
    private Integer adjLineCnt;

    @Column(name = "self_tin")
    private Integer selfTin;

    @Lob
    @Column(name = "self_tin_list", columnDefinition = "TEXT")
    private String selfTinList;

    @Column(name = "sub_pkt_type_tcp")
    private String subPktTypeTCP;

    @Column(name = "sub_pkt_len_tcp")
    private Integer subPktLenTCP;

    @Column(name = "track_cond_cnt")
    private Integer trackCondCnt;

    @Lob
    @Column(name = "track_cond_info", columnDefinition = "TEXT")
    private String trackCondInfo; // JSON-serialized List<Map<String, Object>>

    @Column(name = "sub_packet_tsrp")
    private String subPacketTSRP;

    @Column(name = "sub_packet_length_tsrp")
    private Integer subPacketLengthTSRP;

    @Column(name = "tsr_status")
    private String tsrStatus;

    @Column(name = "tsr_count")
    private Integer tsrCount;

    @Lob
    @Column(name = "tsr_info", columnDefinition = "TEXT")
    private String tsrInfo; // JSON-serialized List<Map<String, Object>>

    @Column(name = "mac_code")
    private String macCode;

    @Column(name = "created_at")
    private LocalDateTime createdAt;
}
