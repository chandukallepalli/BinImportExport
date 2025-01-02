// package com.example.poc.entity;



// import jakarta.persistence.Column;
// import jakarta.persistence.Entity;
// import jakarta.persistence.GeneratedValue;
// import jakarta.persistence.GenerationType;
// import jakarta.persistence.Id;
// import jakarta.persistence.Table;
// import java.time.LocalDateTime;

// import com.fasterxml.jackson.annotation.JsonFormat;

// @Entity
// @Table(name = "new_nms_access_request")
// public class AccessRequestPacket {

//     @Id
//     @GeneratedValue(strategy = GenerationType.IDENTITY)
//     private Long id;

//     @Column(name = "message_length")
//     private Integer messageLength;

//     @Column(name = "message_sequence")
//     private Integer messageSequence;

//     @Column(name = "stationary_kavach_id")
//     private Integer stationaryKavachId;

//     @Column(name = "nms_system_id")
//     private Integer nmsSystemId;

//     @Column(name = "system_version")
//     private Integer systemVersion;

//     @Column(name = "date")
//     private String date; // Assuming format 'YYYY-MM-DD'

//     @Column(name = "time")
//     private String time; // Assuming format 'HH:MM:SS'

//     @Column(name = "station_active_radio", length = 70)
//     private String stationActiveRadio;

//     @Column(name = "crc", length = 10)
//     private String crc;

//     @Column(name = "packet_name", length = 50)
//     private String packetName;

//     @Column(name = "packet_length_bits")
//     private Integer packetLengthBits;

//     @Column(name = "frame_number")
//     private Integer frameNumber;

//     @Column(name = "source_loco_id")
//     private Integer sourceLocoId;

//     @Column(name = "source_loco_version")
//     private String sourceLocoVersion;

//     @Column(name = "abs_loco_loc")
//     private Integer absLocoLoc;

//     @Column(name = "train_length")
//     private Integer trainLength;

//     @Column(name = "train_speed")
//     private Integer trainSpeed;

//     @Column(name = "movement_dir", length = 100)
//     private String movementDir;

//     @Column(name = "emergency_status", length = 100)
//     private String emergencyStatus;

//     @Column(name = "loco_mode", length = 100)
//     private String locoMode;

//     @Column(name = "approaching_station_id")
//     private Integer approachingStationId;

//     @Column(name = "last_rfid_tag")
//     private Integer lastRfidTag;

//     @Column(name = "tin")
//     private Integer tin;

//     @Column(name = "longitude")
//     private Integer longitude;

//     @Column(name = "latitude")
//     private Integer latitude;

//     @Column(name = "loco_rnd_num_rl")
//     private Integer locoRndNumRl;

//     @Column(name = "packet_crc", length = 50)
//     private String packetCrc;

//     @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss.SSSSSS")
//     private LocalDateTime createdAt;

//     // Getters and Setters
//     public Long getId() {
//         return id;
//     }

//     public void setId(Long id) {
//         this.id = id;
//     }

//     public Integer getMessageLength() {
//         return messageLength;
//     }

//     public void setMessageLength(Integer messageLength) {
//         this.messageLength = messageLength;
//     }

//     public Integer getMessageSequence() {
//         return messageSequence;
//     }

//     public void setMessageSequence(Integer messageSequence) {
//         this.messageSequence = messageSequence;
//     }

//     public Integer getStationaryKavachId() {
//         return stationaryKavachId;
//     }

//     public void setStationaryKavachId(Integer stationaryKavachId) {
//         this.stationaryKavachId = stationaryKavachId;
//     }

//     public Integer getNmsSystemId() {
//         return nmsSystemId;
//     }

//     public void setNmsSystemId(Integer nmsSystemId) {
//         this.nmsSystemId = nmsSystemId;
//     }

//     public Integer getSystemVersion() {
//         return systemVersion;
//     }

//     public void setSystemVersion(Integer systemVersion) {
//         this.systemVersion = systemVersion;
//     }

//     public String getDate() {
//         return date;
//     }

//     public void setDate(String date) {
//         this.date = date;
//     }

//     public String getTime() {
//         return time;
//     }

//     public void setTime(String time) {
//         this.time = time;
//     }

//     public String getStationActiveRadio() {
//         return stationActiveRadio;
//     }

//     public void setStationActiveRadio(String stationActiveRadio) {
//         this.stationActiveRadio = stationActiveRadio;
//     }

//     public String getCrc() {
//         return crc;
//     }

//     public void setCrc(String crc) {
//         this.crc = crc;
//     }

//     public String getPacketName() {
//         return packetName;
//     }

//     public void setPacketName(String packetName) {
//         this.packetName = packetName;
//     }

//     public Integer getPacketLengthBits() {
//         return packetLengthBits;
//     }

//     public void setPacketLengthBits(Integer packetLengthBits) {
//         this.packetLengthBits = packetLengthBits;
//     }

//     public Integer getFrameNumber() {
//         return frameNumber;
//     }

//     public void setFrameNumber(Integer frameNumber) {
//         this.frameNumber = frameNumber;
//     }

//     public Integer getSourceLocoId() {
//         return sourceLocoId;
//     }

//     public void setSourceLocoId(Integer sourceLocoId) {
//         this.sourceLocoId = sourceLocoId;
//     }

//     public String getSourceLocoVersion() {
//         return sourceLocoVersion;
//     }

//     public void setSourceLocoVersion(String sourceLocoVersion) {
//         this.sourceLocoVersion = sourceLocoVersion;
//     }

//     public Integer getAbsLocoLoc() {
//         return absLocoLoc;
//     }

//     public void setAbsLocoLoc(Integer absLocoLoc) {
//         this.absLocoLoc = absLocoLoc;
//     }

//     public Integer getTrainLength() {
//         return trainLength;
//     }

//     public void setTrainLength(Integer trainLength) {
//         this.trainLength = trainLength;
//     }

//     public Integer getTrainSpeed() {
//         return trainSpeed;
//     }

//     public void setTrainSpeed(Integer trainSpeed) {
//         this.trainSpeed = trainSpeed;
//     }

//     public String getMovementDir() {
//         return movementDir;
//     }

//     public void setMovementDir(String movementDir) {
//         this.movementDir = movementDir;
//     }

//     public String getEmergencyStatus() {
//         return emergencyStatus;
//     }

//     public void setEmergencyStatus(String emergencyStatus) {
//         this.emergencyStatus = emergencyStatus;
//     }

//     public String getLocoMode() {
//         return locoMode;
//     }

//     public void setLocoMode(String locoMode) {
//         this.locoMode = locoMode;
//     }

//     public Integer getApproachingStationId() {
//         return approachingStationId;
//     }

//     public void setApproachingStationId(Integer approachingStationId) {
//         this.approachingStationId = approachingStationId;
//     }

//     public Integer getLastRfidTag() {
//         return lastRfidTag;
//     }

//     public void setLastRfidTag(Integer lastRfidTag) {
//         this.lastRfidTag = lastRfidTag;
//     }

//     public Integer getTin() {
//         return tin;
//     }

//     public void setTin(Integer tin) {
//         this.tin = tin;
//     }

//     public Integer getLongitude() {
//         return longitude;
//     }

//     public void setLongitude(Integer longitude) {
//         this.longitude = longitude;
//     }

//     public Integer getLatitude() {
//         return latitude;
//     }

//     public void setLatitude(Integer latitude) {
//         this.latitude = latitude;
//     }

//     public Integer getLocoRndNumRl() {
//         return locoRndNumRl;
//     }

//     public void setLocoRndNumRl(Integer locoRndNumRl) {
//         this.locoRndNumRl = locoRndNumRl;
//     }

//     public String getPacketCrc() {
//         return packetCrc;
//     }

//     public void setPacketCrc(String packetCrc) {
//         this.packetCrc = packetCrc;
//     }

//     public LocalDateTime getCreatedAt() {
//         return createdAt;
//     }

//     public void setCreatedAt(LocalDateTime createdAt) {
//         this.createdAt = createdAt;
//     }
// }
