package com.example.poc.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.poc.entity.FieldElementsStatus;
import com.example.poc.entity.FieldElementsStatusRequest;
import com.example.poc.entity.HeartBeatMessage;
import com.example.poc.entity.PDIVersionCheckSskToPsk;
import com.example.poc.entity.PdiVersionCheckPskToSsk;
import com.example.poc.entity.TSLAuthority;
import com.example.poc.entity.TSLRequest;
import com.example.poc.entity.TrainHandoverCancellation;
import com.example.poc.entity.TrainHandoverCancellationAcknowledgement;
import com.example.poc.entity.TrainHandoverRequest;
import com.example.poc.entity.TrainRRi;
import com.example.poc.entity.TrainTakenOver;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
@Service
public class AdjecentKavachBatch {
    @Autowired
    private ObjectMapper objectMapper;
      public int decodeMessageType14(
        byte[] data,
        int offset,
        int startPosition,
        List<PdiVersionCheckPskToSsk> pdiVersionCheckPskToSskBatch,
        List<PDIVersionCheckSskToPsk> pdiVersionCheckSskToPsksBaList,
        List<HeartBeatMessage> heartbeatBatch,
        List<TrainHandoverRequest> trainHandoverRequestBatch,
        List<TrainTakenOver> trainTakenOverBatch,
        List<TrainHandoverCancellation> trainHandoverCancellationBatch,
        List<TSLRequest> tslRequestBatch,
        List<TrainHandoverCancellationAcknowledgement> trainHandoverCancellationAcknowledgementBatch,
        List<FieldElementsStatus> fieldElementsStatusBatch,
        List<FieldElementsStatusRequest> fieldElementsStatusRequestBatch,
        List<TSLAuthority> tslAuthorityBatch,
        List<TrainRRi> trainRRiBatch
) throws JsonProcessingException {
        int messageLength = CommonFile.byteArrayToInt(data, offset, 2);
        offset += 2;
        int messageSequence = CommonFile.byteArrayToInt(data, offset, 2);
        offset += 2;
        int stationaryKavachId = CommonFile.byteArrayToInt(data, offset, 2);
        offset += 2;
        int nmsSystemId = CommonFile.byteArrayToInt(data, offset, 2);
        offset += 2;
        int systemVersion = CommonFile.byteArrayToInt(data, offset, 1);
        offset += 1;
        String date = CommonFile.decodeDate(data, offset);
        offset += 3;
        String time = CommonFile.decodeTime(data, offset);
        offset += 3;
        String specificProtocol = CommonFile.bytesToHex(data, offset, 1);
        offset += 1;

        PdiVersionCheckPskToSsk pdiVersionCheckPsk = null;
        PDIVersionCheckSskToPsk pdiVersionCheckSskToPsk = null;
        HeartBeatMessage heartBeatMessage = null;
        TrainHandoverRequest trainHandoverRequest = null;
        TrainTakenOver trainTakenOver = null;
        TrainHandoverCancellation trainHandoverCancellation = null;
        TSLRequest tslRequest = null;
        TSLAuthority tslAuthority = null;
        FieldElementsStatusRequest fieldElementsStatusRequest = null;
        FieldElementsStatus fieldElementsStatus = null;
        TrainHandoverCancellationAcknowledgement trainHandoverCancellationAcknowledgement = null;
        TrainRRi trainRRi = null;

        String messageType = CommonFile.bytesToHex(data, offset, 2);
        offset += 2;
        switch (messageType) {
            case "0101":
                pdiVersionCheckPsk = new PdiVersionCheckPskToSsk();
                pdiVersionCheckPsk.setPacketName("Command PDI Version Check");
                pdiVersionCheckPsk.setMessageLength(messageLength);
                pdiVersionCheckPsk.setMessageSequence(messageSequence);
                pdiVersionCheckPsk.setStationaryKavachId(stationaryKavachId);
                pdiVersionCheckPsk.setNmsSystemId(nmsSystemId);
                pdiVersionCheckPsk.setSystemVersion(systemVersion);
                pdiVersionCheckPsk.setDate(date);
                pdiVersionCheckPsk.setTime(time);
                pdiVersionCheckPsk.setSpecificProtocol(specificProtocol);
                pdiVersionCheckPsk.setSenderIdentifier(CommonFile.byteArrayToInt(data, offset, 20));
                offset += 20;
                pdiVersionCheckPsk.setReceiverIdentifier(CommonFile.byteArrayToInt(data, offset, 20));
                offset += 20;
                pdiVersionCheckPsk.setPacketLength(CommonFile.byteArrayToInt(data, offset, 2));
                offset += 2;
                pdiVersionCheckPsk.setPdiVersion(CommonFile.byteArrayToInt(data, offset, 1));
                offset += 1;
                pdiVersionCheckPsk.setRp(CommonFile.byteArrayToInt(data, offset, 2));
                offset += 2;
                break;
            case "0102":
                pdiVersionCheckSskToPsk = new PDIVersionCheckSskToPsk();
                pdiVersionCheckSskToPsk.setPacketName("Message PDI Version Check");
                pdiVersionCheckSskToPsk.setMessageLength(messageLength);
                pdiVersionCheckSskToPsk.setMessageSequence(messageSequence);
                pdiVersionCheckSskToPsk.setStationaryKavachId(stationaryKavachId);
                pdiVersionCheckSskToPsk.setNmsSystemId(nmsSystemId);
                pdiVersionCheckSskToPsk.setSystemVersion(systemVersion);
                pdiVersionCheckSskToPsk.setDate(date);
                pdiVersionCheckSskToPsk.setTime(time);
                pdiVersionCheckSskToPsk.setSpecificProtocol(specificProtocol);
                pdiVersionCheckSskToPsk.setSenderIdentifier(CommonFile.byteArrayToInt(data, offset, 20));
                offset += 20;
                pdiVersionCheckSskToPsk.setReceiverIdentifier(CommonFile.byteArrayToInt(data, offset, 20));
                offset += 20;
                pdiVersionCheckSskToPsk.setPacketLength(CommonFile.byteArrayToInt(data, offset, 2));
                offset += 2;
                pdiVersionCheckSskToPsk.setResultPdiVersionCheck(CommonFile.byteArrayToInt(data, offset, 1));
                offset += 1;
                pdiVersionCheckSskToPsk.setPdiVersionSsk(CommonFile.byteArrayToInt(data, offset, 1));
                offset += 1;
                pdiVersionCheckSskToPsk.setRN(CommonFile.byteArrayToInt(data, offset, 2));
                offset += 1;
                pdiVersionCheckSskToPsk.setMacCode(CommonFile.bytesToHex(data, offset, 4));
                offset += 4;
                break;
            case "0103":
                heartBeatMessage = new HeartBeatMessage();
                heartBeatMessage.setPacketName("Heart Beat");
                heartBeatMessage.setMessageLength(messageLength);
                heartBeatMessage.setMessageSequence(messageSequence);
                heartBeatMessage.setStationaryKavachId(stationaryKavachId);
                heartBeatMessage.setNmsSystemId(nmsSystemId);
                heartBeatMessage.setSystemVersion(systemVersion);
                heartBeatMessage.setDate(date);
                heartBeatMessage.setTime(time);
                heartBeatMessage.setSpecificProtocol(specificProtocol);
                heartBeatMessage.setPrimarySecondarySKavach(CommonFile.byteArrayToInt(data, offset, 20));
                offset += 20;
                heartBeatMessage.setSecondarySecondarySKavach(CommonFile.byteArrayToInt(data, offset, 20));
                offset += 20;
                heartBeatMessage.setPacketMessageLength(CommonFile.byteArrayToInt(data, offset, 2));
                offset += 2;
                heartBeatMessage.setFrameNumber(CommonFile.byteArrayToInt(data, offset, 3));
                offset += 3;
                heartBeatMessage.setPacketMessageSequence(CommonFile.byteArrayToInt(data, offset, 2));
                offset += 2;
                heartBeatMessage.setMacCode(CommonFile.bytesToHex(data, offset, 4));
                offset += 4;
                break;
            case "0104":
                trainHandoverRequest = new TrainHandoverRequest();
                trainHandoverRequest.setPacketName("Train Handover Request");
                trainHandoverRequest.setMessageLength(messageLength);
                trainHandoverRequest.setMessageSequence(messageSequence);
                trainHandoverRequest.setStationaryKavachId(stationaryKavachId);
                trainHandoverRequest.setNmsSystemId(nmsSystemId);
                trainHandoverRequest.setSystemVersion(systemVersion);
                trainHandoverRequest.setDate(date);
                trainHandoverRequest.setTime(time);
                trainHandoverRequest.setSpecificProtocol(specificProtocol);
                trainHandoverRequest.setSenderIdentifier(CommonFile.byteArrayToInt(data, offset, 20));
                offset += 20;
                trainHandoverRequest.setReceiverIdentifier(CommonFile.byteArrayToInt(data, offset, 20));
                offset += 20;
                trainHandoverRequest.setPacketMessageLength(CommonFile.byteArrayToInt(data, offset, 2));
                offset += 2;
                trainHandoverRequest.setFrameNumber(CommonFile.byteArrayToInt(data, offset, 3));
                offset += 3;
                trainHandoverRequest.setPacketMessageSequence(CommonFile.byteArrayToInt(data, offset, 2));
                offset += 2;
                trainHandoverRequest.setBorderRfidTag(CommonFile.byteArrayToInt(data, offset, 2));
                offset += 2;
                trainHandoverRequest.setInSections(CommonFile.bytesToHex(data, offset, 1));
                offset += 1;
                trainHandoverRequest.setDistToBorderTagLocation(CommonFile.byteArrayToInt(data, offset, 2));
                offset += 2;
                int m = offset * 8;
                trainHandoverRequest.setOnboardPacketName(CommonFile.extractBitsAsString(data, m, 4));
                m += 4;
                trainHandoverRequest.setOnPacketLength(CommonFile.extractBits(data, m, 7));
                m += 7;
                trainHandoverRequest.setOnFrameNumber(CommonFile.extractBits(data, m, 17));
                m += 17;
                trainHandoverRequest.setSourceLocoId(CommonFile.extractBits(data, m, 20));
                m += 20;
                trainHandoverRequest.setSourceLocoVersion(CommonFile.extractBits(data, m, 3));
                m += 3;
                trainHandoverRequest.setAbsLocoLoc(CommonFile.extractBits(data, m, 23));
                m += 23;
                trainHandoverRequest.setLDoubtOver(CommonFile.extractBits(data, m, 9));
                m += 9;
                trainHandoverRequest.setLDoubtUnder(CommonFile.extractBits(data, m, 9));
                m += 9;
                trainHandoverRequest.setTrainInt(CommonFile.extractBitsAsString(data, m, 2));
                m += 2;
                trainHandoverRequest.setTrainLength(CommonFile.extractBits(data, m, 11));
                m += 11;
                trainHandoverRequest.setTrainSpeed( CommonFile.extractBits(data, m, 9));
                m += 9;
                trainHandoverRequest.setMovementDir(CommonFile.extractBitsAsString(data, m, 2));
                m += 2;
                trainHandoverRequest.setEmergencyStatus(CommonFile.extractBitsAsString(data, m, 3));
                m += 3;
                trainHandoverRequest.setLocoMode(CommonFile.extractBitsAsString(data, m, 4));
                m += 4;
                trainHandoverRequest.setLastRfidTag(CommonFile.extractBits(data, m, 10));
                m += 10;
                trainHandoverRequest.setTagDup(CommonFile.extractBitsAsString(data, m, 1));
                m += 1;
                trainHandoverRequest.setTagLinkInfo(CommonFile.extractBitsAsString(data, m, 3));
                m += 3;
                trainHandoverRequest.setTin(CommonFile.extractBits(data, m, 9));
                m += 9;
                trainHandoverRequest.setBreakApplied(CommonFile.extractBits(data, m, 3));
                m += 3;
                trainHandoverRequest.setNewMaReply(CommonFile.extractBits(data, m, 2));
                m += 2;
                trainHandoverRequest.setLastRefProfileNum(CommonFile.extractBits(data, m, 4));
                m += 4;
                trainHandoverRequest.setSigOv(CommonFile.extractBitsAsString(data, m, 1));
                m += 1;
                trainHandoverRequest.setInfoAck(CommonFile.extractBits(data, m, 4));
                m += 4;
                trainHandoverRequest.setSpare(CommonFile.extractBits(data, m, 2));
                m += 2;
                trainHandoverRequest.setLocoHealthStatus(CommonFile.extractBits(data, m, 6));
                m += 6;
                offset = m / 8;
                trainHandoverRequest.setMacCode(CommonFile.bytesToHex(data, offset, 4));
                offset += 4;
                break;
            case "0105":
                trainRRi = new TrainRRi();
                trainRRi.setPacketName("Train RRI");
                trainRRi.setMessageLength(messageLength);
                trainRRi.setMessageSequence(messageSequence);
                trainRRi.setStationaryKavachId(stationaryKavachId);
                trainRRi.setNmsSystemId(nmsSystemId);
                trainRRi.setSystemVersion(systemVersion);
                trainRRi.setDate(date);
                trainRRi.setTime(time);
                trainRRi.setSpecificProtocol(specificProtocol);
                trainRRi.setSenderIdentifier(CommonFile.byteArrayToInt(data, offset, 20));
                offset += 20;
                trainRRi.setReceiverIdentifier(CommonFile.byteArrayToInt(data, offset, 20));
                offset += 20;
                trainRRi.setPacketMessageLength(CommonFile.byteArrayToInt(data, offset, 2));
                offset += 2;
                trainRRi.setFrameNumber(CommonFile.byteArrayToInt(data, offset, 3));
                offset += 3;
                trainRRi.setPacketMessageSequence(CommonFile.byteArrayToInt(data, offset, 2));
                offset += 2;
                trainRRi.setBorderRfidTag(CommonFile.byteArrayToInt(data, offset, 2));
                offset += 2;
                trainRRi.setRefProfileId(CommonFile.byteArrayToInt(data, offset, 1));
                offset += 1;
                trainRRi.setOnboardKavachIdentity(CommonFile.byteArrayToInt(data, offset, 3));
                offset += 3;
                m = offset * 8;
                String SUB_PKT_TYPE = CommonFile.extractBitsAsString(data, m, 4);

                if (SUB_PKT_TYPE.equals("0000")) {
                    trainRRi.setSubPktType("Movement Authority");
                    m = m + 4;
                    int subPacketLengthMa = CommonFile.extractBits(data, m, 7);
                    m = m + 7;
                    trainRRi.setSubPktLenMA(subPacketLengthMa);
                    trainRRi.setFrameOffset(CommonFile.extractBitsAsString(data, m, 4));
                    m = m + 4;
                    String DEST_LOCO_SOS = CommonFile.extractBitsAsString(data, m, 4);
                    switch (DEST_LOCO_SOS) {
                        case "0000":
                            DEST_LOCO_SOS = "No SoS / Emergency";
                            break;
                        case "0001":
                            DEST_LOCO_SOS = "Foreign RFID";
                            break;
                        case "0010":
                            DEST_LOCO_SOS = "Reserved";
                            break;
                        case "0011":
                            DEST_LOCO_SOS = "Onboard Odo error is >= 120m";
                            break;
                        case "0100":
                            DEST_LOCO_SOS = "Detection of SPAD";
                            break;
                        case "0101":
                            DEST_LOCO_SOS = "Rear-end collision";
                            break;
                        case "0110":
                            DEST_LOCO_SOS = "Head-On collision";
                            break;
                        case "0111":
                            DEST_LOCO_SOS = "Violation of Shunting limits in shunt mode";
                            break;
                        case "1000":
                            DEST_LOCO_SOS = "Station General SoS";
                            break;
                        default:
                            DEST_LOCO_SOS = "Reserved or Unknown";
                            break;
                    }

                    trainRRi.setDstLocoSos(DEST_LOCO_SOS);
                    m = m + 4;
                    String TRAIN_SECTION_TYPE = CommonFile.extractBitsAsString(data, m, 2);
                    switch (TRAIN_SECTION_TYPE) {
                        case "00":
                            TRAIN_SECTION_TYPE = "Station Section";
                            break;
                        case "01":
                            TRAIN_SECTION_TYPE = "Absolute Block";
                            break;
                        case "10":
                            TRAIN_SECTION_TYPE = "Autoblock";
                            break;
                        case "11":
                            TRAIN_SECTION_TYPE = "Reserved";
                            break;
                        default:
                            TRAIN_SECTION_TYPE = "Unknown";
                    }
                    trainRRi.setTrainSectionType(TRAIN_SECTION_TYPE);
                    m = m + 2;
                    String signalInfo = CommonFile.extractBitsAsString(data, m, 17);
                    String lineNumberField = signalInfo.substring(12, 17);
                    switch (lineNumberField) {
                        case "00000":
                            lineNumberField = "To be sent when line number information is not applicable";
                            break;
                        case "11111":
                            lineNumberField = "Line Number in excess of 30 Decimal";
                            break;
                        case "11110":
                            lineNumberField = "Goods Lines";
                            break;
                        default:
                            lineNumberField = "Unknown Line Number";
                            break;
                    }
                    trainRRi.setLineNumber(lineNumberField);

                    String lineName = signalInfo.substring(8, 12);
                    switch (lineName) {
                        case "0000":
                            lineName = "Up Signal";
                            break;
                        case "0001":
                            lineName = "Down Signal";
                            break;
                        case "0010":
                            lineName = "Up Fast Signal";
                            break;
                        case "0011":
                            lineName = "Down Fast Signal";
                            break;
                        case "1000":
                            lineName = "Up Slow Signal";
                            break;
                        case "1001":
                            lineName = "Down Slow Signal";
                            break;
                        case "1010":
                            lineName = "Up Main Signal";
                            break;
                        case "1011":
                            lineName = "Down Main Signal";
                            break;
                        case "1100":
                            lineName = "Up Sub Signal";
                            break;
                        case "1101":
                            lineName = "Down Sub Signal";
                            break;
                        case "1110":
                            lineName = "UP BI-Direction";
                            break;
                        case "1111":
                            lineName = "DN BI-Direction";
                            break;
                        default:
                            lineName = "Future Use or Reserved";
                            break;
                    }
                    trainRRi.setLineName(lineName);

                    String typeOfSignal = signalInfo.substring(2, 8); // Bits a9 to a14 (next 6 bits from the right)

                    switch (typeOfSignal) {
                        case "000000":
                            typeOfSignal = "Undefined";
                            break;
                        case "010000":
                            typeOfSignal = "Distant Signal";
                            break;
                        case "010001":
                            typeOfSignal = "Inner Distant Signal";
                            break;
                        case "010010":
                            typeOfSignal = "Gate Distant Signal";
                            break;
                        case "010011":
                            typeOfSignal = "Gate Inner Distant Signal";
                            break;
                        case "010100":
                            typeOfSignal = "IB Distant Signal";
                            break;
                        case "010101":
                            typeOfSignal = "IB Inner Distant Signal";
                            break;
                        case "010110":
                            typeOfSignal = "Auto Signal (Excludes Gate Stop Signal in Auto Territory)";
                            break;
                        case "010111":
                            typeOfSignal = "Semi-Automatic Signal with A-marker lit";
                            break;
                        case "100100":
                            typeOfSignal = "Semi-Automatic Signal without A marker lit";
                            break;
                        case "011000":
                            typeOfSignal = "Main Home without Junction Route Indicator";
                            break;
                        case "011001":
                            typeOfSignal = "Main Home with Junction Route Indicator";
                            break;
                        case "011010":
                            typeOfSignal = "Routing Home without Junction Type Route Indicator";
                            break;
                        case "011011":
                            typeOfSignal = "Routing Home with Junction Type Route Indicator";
                            break;
                        case "011100":
                            typeOfSignal = "Mainline Starter";
                            break;
                        case "011101":
                            typeOfSignal = "Loopline Starter";
                            break;
                        case "011110":
                            typeOfSignal = "Intermediate Starter";
                            break;
                        case "000001":
                            typeOfSignal = "Advanced Starter Signal";
                            break;
                        case "000010":
                            typeOfSignal = "IB Stop Signal";
                            break;
                        case "000011":
                            typeOfSignal = "Gate Stop Signal";
                            break;
                        case "000100":
                            typeOfSignal = "Calling-on Signal";
                            break;
                        case "000101":
                            typeOfSignal = "Advanced Starter-cum-Gate Signal";
                            break;
                        case "000110":
                            typeOfSignal = "Gate-cum-Distant Signal";
                            break;
                        case "000111":
                            typeOfSignal = "Advanced Starter-cum-Distant Signal";
                            break;
                        case "100011":
                            typeOfSignal = "Gate Stop Signal in Auto Territory";
                            break;
                        case "100101":
                            typeOfSignal = "Advance Starter-cum-Gate Inner Distant Signal";
                            break;
                        case "100110":
                            typeOfSignal = "Gate-cum-Inner Distant Signal";
                            break;
                        case "100111":
                            typeOfSignal = "Gate Inner Distant-cum-Distant Signal";
                            break;
                        case "101000":
                            typeOfSignal = "IB Signal-cum-Gate Distant Signal";
                            break;
                        case "101001":
                            typeOfSignal = "IB Signal-cum-Gate Inner Distant Signal";
                            break;
                        case "101010":
                            typeOfSignal = "IB Signal-cum-Distant Signal";
                            break;
                        case "101011":
                            typeOfSignal = "Advanced Starter-cum-IB Distant";
                            break;
                        case "101100":
                            typeOfSignal = "Starter-cum- IB Distant Signal";
                            break;
                        case "101101":
                            typeOfSignal = "Stop Board/Buffer Stop";
                            break;
                        case "101110":
                            typeOfSignal = "Gate cum IB Distant Signal";
                            break;
                        case "101111":
                            typeOfSignal = "Gate cum IB Inner Distant Signal";
                            break;
                        case "110000":
                            typeOfSignal = "Advance Starter-cum-Gate Distant Signal";
                            break;
                        case "001000":
                            typeOfSignal = "Only in RFID Tag, not in Radio Packet";
                            break;
                        default:
                            typeOfSignal = "Unknown or Reserved";
                            break;
                    }

                    trainRRi.setTypeOfSignal(typeOfSignal);

                    int signalOverridePermission = Integer.parseInt(signalInfo.substring(1, 2)); // Bit a15 (1 bit
                    String signalOverride; // from the right)
                    switch (signalOverridePermission) {
                        case 0:
                            signalOverride = "AtStandstill";
                            break;
                        case 1:
                            signalOverride = "WhileRunning";
                            break;
                        default:
                            signalOverride = "Unknown";
                            break;
                    }

                    trainRRi.setSignalOv(signalOverride);

                    int stopSignal = Integer.parseInt(signalInfo.substring(0, 1)); // Bit a16 (rightmost 1 bit)
                    String stopSignalDescription;

                    switch (stopSignal) {
                        case 0:
                            stopSignalDescription = "No";
                            break;
                        case 1:
                            stopSignalDescription = "Yes";
                            break;
                        default:
                            stopSignalDescription = "Unknown";
                            break;
                    }
                    trainRRi.setStopSignal(stopSignalDescription);
                    m += 17;

                    String CUR_SIG_ASPECT = CommonFile.extractBitsAsString(data, m, 6);

                    switch (CUR_SIG_ASPECT) {
                        case "000000":
                            CUR_SIG_ASPECT = "Undefined";
                            break;
                        case "000001":
                            CUR_SIG_ASPECT = "Red";
                            break;
                        case "000010":
                            CUR_SIG_ASPECT = "Yellow without Display of Route Indication";
                            break;
                        case "000011":
                            CUR_SIG_ASPECT = "Yellow with Pos1 Junction Type Route Indication (left)";
                            break;
                        case "000100":
                            CUR_SIG_ASPECT = "Yellow with Pos2 Junction Type Route Indication (left)";
                            break;
                        case "000101":
                            CUR_SIG_ASPECT = "Yellow with Pos3 Junction Type Route Indication (left)";
                            break;
                        case "000110":
                            CUR_SIG_ASPECT = "Yellow with Pos4 Junction Type Route Indication (left)";
                            break;
                        case "000111":
                            CUR_SIG_ASPECT = "Yellow with Pos5 Junction Type Route Indication (left)";
                            break;
                        case "001000":
                            CUR_SIG_ASPECT = "Yellow with Pos6 Junction Type Route Indication (left)";
                            break;
                        case "001001":
                            CUR_SIG_ASPECT = "Spare";
                            break;
                        case "001010":
                            CUR_SIG_ASPECT = "Double Yellow";
                            break;
                        case "001011":
                            CUR_SIG_ASPECT = "Green";
                            break;
                        case "001100":
                            CUR_SIG_ASPECT = "Double Yellow with Pos1 Junction Type Route Indication (left)";
                            break;
                        case "001101":
                            CUR_SIG_ASPECT = "Double Yellow with Pos4 Junction Type Route Indication (right)";
                            break;
                        case "001110":
                            CUR_SIG_ASPECT = "AG Marker OFF";
                            break;
                        case "001111":
                            CUR_SIG_ASPECT = "RED with Callin-on at OFF";
                            break;
                        case "011000":
                            CUR_SIG_ASPECT = "Stop Board / Buffer Stop";
                            break;
                        case "010000":
                            CUR_SIG_ASPECT = "Spare";
                        case "010001":
                            CUR_SIG_ASPECT = "Spare";
                        case "010010":
                            CUR_SIG_ASPECT = "Spare";
                        case "010011":
                            CUR_SIG_ASPECT = "Spare";
                        case "010100":
                            CUR_SIG_ASPECT = "Spare";
                        case "010101":
                            CUR_SIG_ASPECT = "Spare";
                        case "010110":
                            CUR_SIG_ASPECT = "Spare";
                        case "010111":
                            CUR_SIG_ASPECT = "Spare";
                        case "011001":
                            CUR_SIG_ASPECT = "Spare";
                        case "011010":
                            CUR_SIG_ASPECT = "Spare";
                        case "011011":
                            CUR_SIG_ASPECT = "Spare";
                        case "011100":
                            CUR_SIG_ASPECT = "Spare";
                        case "011101":
                            CUR_SIG_ASPECT = "Spare";
                        case "011110":
                            CUR_SIG_ASPECT = "Spare";
                        case "011111":
                            CUR_SIG_ASPECT = "Spare";
                            break;
                        case "100000":
                            CUR_SIG_ASPECT = "Spare";
                            break;
                        case "100001":
                            CUR_SIG_ASPECT = "Spare";
                            break;
                        case "100010":
                            CUR_SIG_ASPECT = "Spare";
                            break;
                        case "100011":
                            CUR_SIG_ASPECT = "Spare";
                            break;
                        case "100100":
                            CUR_SIG_ASPECT = "Spare";
                            break;
                        case "100101":
                            CUR_SIG_ASPECT = "Spare";
                            break;
                        case "100110":
                            CUR_SIG_ASPECT = "Spare";
                            break;
                        case "100111":
                            CUR_SIG_ASPECT = "Spare";
                            break;
                        case "101000":
                            CUR_SIG_ASPECT = "Spare";
                            break;
                        case "101001":
                            CUR_SIG_ASPECT = "Spare";
                            break;
                        case "101010":
                            CUR_SIG_ASPECT = "Spare";
                            break;
                        case "101011":
                            CUR_SIG_ASPECT = "Spare";
                            break;
                        case "101100":
                            CUR_SIG_ASPECT = "Spare";
                            break;
                        case "101101":
                            CUR_SIG_ASPECT = "Spare";
                            break;
                        case "101110":
                            CUR_SIG_ASPECT = "Spare";
                            break;
                        case "101111":
                            CUR_SIG_ASPECT = "Spare";
                            break;
                        case "110000":
                            CUR_SIG_ASPECT = "Spare";
                            break;
                        case "110001":
                            CUR_SIG_ASPECT = "Spare";
                            break;
                        case "110010":
                            CUR_SIG_ASPECT = "Spare";
                            break;
                        case "110011":
                            CUR_SIG_ASPECT = "Spare";
                            break;
                        case "110100":
                            CUR_SIG_ASPECT = "Spare";
                            break;
                        case "110101":
                            CUR_SIG_ASPECT = "Spare";
                            break;
                        case "110110":
                            CUR_SIG_ASPECT = "Spare";
                            break;
                        case "110111":
                            CUR_SIG_ASPECT = "Spare";
                            break;
                        case "111000":
                            CUR_SIG_ASPECT = "Spare";
                            break;
                        case "111001":
                            CUR_SIG_ASPECT = "Spare";
                            break;
                        case "111010":
                            CUR_SIG_ASPECT = "Spare";
                            break;
                        case "111011":
                            CUR_SIG_ASPECT = "Spare";
                            break;
                        case "111100":
                            CUR_SIG_ASPECT = "Spare";
                            break;
                        case "111101":
                            CUR_SIG_ASPECT = "Spare";
                            break;
                        case "111110":
                            CUR_SIG_ASPECT = "Spare";
                            break;
                        case "111111":
                            CUR_SIG_ASPECT = "Yellow with Stencil route 1 to 32";
                            break;
                        default:
                            CUR_SIG_ASPECT = "Unknown";
                            break;
                    }
                    trainRRi.setCurrentSigAspect(CUR_SIG_ASPECT);
                    m = m + 6;

                    String NEXT_SIG_ASPECT = CommonFile.extractBitsAsString(data, m, 6);
                    switch (NEXT_SIG_ASPECT) {
                        case "000000":
                            NEXT_SIG_ASPECT = "Undefined";
                            break;
                        case "000001":
                            NEXT_SIG_ASPECT = "Red";
                            break;
                        case "000010":
                            NEXT_SIG_ASPECT = "Yellow without Display of Route Indication";
                            break;
                        case "000011":
                            NEXT_SIG_ASPECT = "Yellow with Pos1 Junction Type Route Indication (left)";
                            break;
                        case "000100":
                            NEXT_SIG_ASPECT = "Yellow with Pos2 Junction Type Route Indication (left)";
                            break;
                        case "000101":
                            NEXT_SIG_ASPECT = "Yellow with Pos3 Junction Type Route Indication (left)";
                            break;
                        case "000110":
                            NEXT_SIG_ASPECT = "Yellow with Pos4 Junction Type Route Indication (left)";
                            break;
                        case "000111":
                            NEXT_SIG_ASPECT = "Yellow with Pos5 Junction Type Route Indication (left)";
                            break;
                        case "001000":
                            NEXT_SIG_ASPECT = "Yellow with Pos6 Junction Type Route Indication (left)";
                            break;
                        case "001001":
                            NEXT_SIG_ASPECT = "Spare";
                            break;
                        case "001010":
                            NEXT_SIG_ASPECT = "Double Yellow";
                            break;
                        case "001011":
                            NEXT_SIG_ASPECT = "Green";
                            break;
                        case "001100":
                            NEXT_SIG_ASPECT = "Double Yellow with Pos1 Junction Type Route Indication (left)";
                            break;
                        case "001101":
                            NEXT_SIG_ASPECT = "Double Yellow with Pos4 Junction Type Route Indication (right)";
                            break;
                        case "001110":
                            NEXT_SIG_ASPECT = "AG Marker OFF";
                            break;
                        case "001111":
                            NEXT_SIG_ASPECT = "RED with Callin-on at OFF";
                            break;
                        case "011000":
                            NEXT_SIG_ASPECT = "Stop Board / Buffer Stop";
                            break;
                        case "010000":
                            NEXT_SIG_ASPECT = "Spare";
                            break;
                        case "010001":
                            NEXT_SIG_ASPECT = "Spare";
                            break;
                        case "010010":
                            NEXT_SIG_ASPECT = "Spare";
                            break;
                        case "010011":
                            NEXT_SIG_ASPECT = "Spare";
                            break;
                        case "010100":
                            NEXT_SIG_ASPECT = "Spare";
                            break;
                        case "010101":
                            NEXT_SIG_ASPECT = "Spare";
                            break;
                        case "010110":
                            NEXT_SIG_ASPECT = "Spare";
                            break;
                        case "010111":
                            NEXT_SIG_ASPECT = "Spare";
                            break;
                        case "011001":
                            NEXT_SIG_ASPECT = "Spare";
                            break;
                        case "011010":
                            NEXT_SIG_ASPECT = "Spare";
                            break;
                        case "011011":
                            NEXT_SIG_ASPECT = "Spare";
                            break;
                        case "011100":
                            NEXT_SIG_ASPECT = "Spare";
                            break;
                        case "011101":
                            NEXT_SIG_ASPECT = "Spare";
                            break;
                        case "011110":
                            NEXT_SIG_ASPECT = "Spare";
                            break;
                        case "011111":
                            NEXT_SIG_ASPECT = "Spare";
                            break;

                        case "100000":
                            NEXT_SIG_ASPECT = "Spare";
                            break;
                        case "100001":
                            NEXT_SIG_ASPECT = "Spare";
                            break;
                        case "100010":
                            NEXT_SIG_ASPECT = "Spare";
                            break;
                        case "100011":
                            NEXT_SIG_ASPECT = "Spare";
                            break;
                        case "100100":
                            NEXT_SIG_ASPECT = "Spare";
                            break;
                        case "100101":
                            NEXT_SIG_ASPECT = "Spare";
                            break;
                        case "100110":
                            NEXT_SIG_ASPECT = "Spare";
                            break;
                        case "100111":
                            NEXT_SIG_ASPECT = "Spare";
                            break;
                        case "101000":
                            NEXT_SIG_ASPECT = "Spare";
                            break;
                        case "101001":
                            NEXT_SIG_ASPECT = "Spare";
                            break;
                        case "101010":
                            NEXT_SIG_ASPECT = "Spare";
                            break;
                        case "101011":
                            NEXT_SIG_ASPECT = "Spare";
                            break;
                        case "101100":
                            NEXT_SIG_ASPECT = "Spare";
                            break;
                        case "101101":
                            NEXT_SIG_ASPECT = "Spare";
                            break;
                        case "101110":
                            NEXT_SIG_ASPECT = "Spare";
                            break;
                        case "101111":
                            NEXT_SIG_ASPECT = "Spare";
                            break;
                        case "110000":
                            NEXT_SIG_ASPECT = "Spare";
                            break;
                        case "110001":
                            NEXT_SIG_ASPECT = "Spare";
                            break;
                        case "110010":
                            NEXT_SIG_ASPECT = "Spare";
                            break;
                        case "110011":
                            NEXT_SIG_ASPECT = "Spare";
                            break;
                        case "110100":
                            NEXT_SIG_ASPECT = "Spare";
                            break;
                        case "110101":
                            NEXT_SIG_ASPECT = "Spare";
                            break;
                        case "110110":
                            NEXT_SIG_ASPECT = "Spare";
                            break;
                        case "110111":
                            NEXT_SIG_ASPECT = "Spare";
                            break;
                        case "111000":
                            NEXT_SIG_ASPECT = "Spare";
                            break;
                        case "111001":
                            NEXT_SIG_ASPECT = "Spare";
                            break;
                        case "111010":
                            NEXT_SIG_ASPECT = "Spare";
                            break;
                        case "111011":
                            NEXT_SIG_ASPECT = "Spare";
                            break;
                        case "111100":
                            NEXT_SIG_ASPECT = "Spare";
                            break;
                        case "111101":
                            NEXT_SIG_ASPECT = "Spare";
                            break;
                        case "111110":
                            NEXT_SIG_ASPECT = "Spare";
                            break;
                        case "111111":
                            NEXT_SIG_ASPECT = "Yellow with Stencil route 1 to 32";
                            break;
                        default:
                            NEXT_SIG_ASPECT = "Unknown";
                            break;
                    }

                    trainRRi.setNextSigAspect(NEXT_SIG_ASPECT);
                    m = m + 6;
                    trainRRi.setApproachingSignalDistance(CommonFile.extractBits(data, m, 15));
                    m = m + 15;
                    String AUTHORITY_TYPE = CommonFile.extractBitsAsString(data, m, 2);
                    switch (AUTHORITY_TYPE) {

                        case "00":
                            AUTHORITY_TYPE = "Not to be used";
                            break;
                        case "01":
                            AUTHORITY_TYPE = "OS Authority";
                            break;
                        case "10":
                            AUTHORITY_TYPE = "FS Authority";
                            break;
                        case "11":
                            AUTHORITY_TYPE = "SR Authority";
                            break;
                        default:
                            AUTHORITY_TYPE = "Unknown";
                            break;
                    }
                    trainRRi.setAuthorityType(AUTHORITY_TYPE);
                    m = m + 2;

                    if ("OS Authority".equals(AUTHORITY_TYPE)) {
                        int AUTHORIZED_SPEED = CommonFile.extractBits(data, m, 6);
                        m += 6;

                        if (AUTHORIZED_SPEED >= 0 && AUTHORIZED_SPEED <= 50) {
                            int speedInKmph = AUTHORIZED_SPEED * 5;
                            trainRRi.setAuthorizedSpeed("Speed: " + speedInKmph + " kmph");
                        } else if (AUTHORIZED_SPEED >= 51 && AUTHORIZED_SPEED <= 61) {
                            trainRRi.setAuthorizedSpeed("Speed: Reserved for future use");
                        } else if (AUTHORIZED_SPEED == 62) {
                            trainRRi.setAuthorizedSpeed(
                                    "Speed: 8 kmph (Configurable) for auto signal override during night");
                        } else if (AUTHORIZED_SPEED == 63) {
                            trainRRi.setAuthorizedSpeed("Speed: Unknown");
                        } else {
                            trainRRi.setAuthorizedSpeed("Speed: Invalid AUTHORIZED_SPEED value");
                        }
                    } else {
                        trainRRi.setAuthorizedSpeed(
                                "AUTHORIZED_SPEED does not follow as AUTHORITY_TYPE is not '01'");
                    }

                    trainRRi.setMaWRTSig(CommonFile.extractBits(data, m, 16));
                    m += 16;

                    String REQ_SHORTEN_MA = CommonFile.extractBitsAsString(data, m, 1);
                    switch (REQ_SHORTEN_MA) {

                        case "0":
                            REQ_SHORTEN_MA = "No Request";
                            break;
                        case "1":
                            REQ_SHORTEN_MA = "New request from trackside for shortening MA";
                            break;
                        default:
                            REQ_SHORTEN_MA = "Unknown";
                            break;
                    }
                    trainRRi.setReqShortenMa(REQ_SHORTEN_MA);
                    m += 1;
                    if (REQ_SHORTEN_MA.equals("New request from trackside for shortening MA")) {
                        trainRRi.setNewMa(CommonFile.extractBitsAsString(data, m, 16));
                        m += 16;
                    }

                    String TRAIN_LENGTH_INFO_STS = CommonFile.extractBitsAsString(data, m, 1);

                    trainRRi.setTrainLengthInfoSts(TRAIN_LENGTH_INFO_STS);
                    m += 1;
                    if (TRAIN_LENGTH_INFO_STS.equals("1")) {
                        trainRRi.setTrnLenInfoType(CommonFile.extractBitsAsString(data, m, 1));
                        m += 1;
                        trainRRi.setRefFrameNumTlm(CommonFile.extractBits(data, m, 17));
                        m += 17;
                        trainRRi.setRefOffsetIntTlm(CommonFile.extractBits(data, m, 8));
                        m += 8;
                    }

                    String NEXT_STN_COMM = CommonFile.extractBitsAsString(data, m, 1);

                    switch (NEXT_STN_COMM) {

                        case "0":
                            trainRRi.setNextStnComm("No_Handover");
                            break;
                        case "1":
                            trainRRi.setNextStnComm("Next_Station_Handover");
                            break;
                        default:
                            trainRRi.setNextStnComm("Unknown");
                            break;
                    }
                    m += 1;
                    if (NEXT_STN_COMM.equals("Next_Station_Handover")) {
                        trainRRi.setApprStnIlcIbsId(CommonFile.extractBits(data, m, 16));
                        m += 16;
                    }
                    int paddingBitsMA = 8 - (m % 8);
                    if (paddingBitsMA < 8) {
                        m += paddingBitsMA;
                    }
                    String SUB_PACKET_1 = CommonFile.extractBitsAsString(data, m, 4);
                    m += 4;
                    if (SUB_PACKET_1.equals("0001")) {
                        trainRRi.setSubPktTypeSSP("STATIC SPEED PROFILE");
                        trainRRi.setSubPktLenSSP(CommonFile.extractBits(data, m, 7));
                        m += 7;
                        int LM_SPEED_INFO_CNT = CommonFile.extractBits(data, m, 5);
                        trainRRi.setSspCountInfo(LM_SPEED_INFO_CNT);
                        m += 5;

                        List<Map<String, Object>> LM_SPEED_INFO = new ArrayList<>(LM_SPEED_INFO_CNT);

                        for (int i = 0; i < LM_SPEED_INFO_CNT; i++) {

                            int LM_STATIC_SPEED_DISTANCE = CommonFile.extractBits(data, m, 15);
                            m += 15;

                            String LM_STATIC_SPEED_CLASS = CommonFile.extractBitsAsString(data, m, 1);
                            m += 1;
                            String Speed_Type;
                            int speedA = 0, speedB = 0, speedC = 0;

                            if (LM_STATIC_SPEED_CLASS.equals("0")) {
                                Speed_Type = "Universal";
                                int speed = CommonFile.extractBits(data, m, 6);
                                speedA = speedB = speedC = speed;
                                m += 6;
                            } else if (LM_STATIC_SPEED_CLASS.equals("1")) {
                                Speed_Type = "Classified Speeds";
                                speedA = CommonFile.extractBits(data, m, 6);
                                m += 6;
                                speedB = CommonFile.extractBits(data, m, 6);
                                m += 6;
                                speedC = CommonFile.extractBits(data, m, 6);
                                m += 6;
                            } else {
                                Speed_Type = "Unknown";
                            }
                            Map<String, Object> speedInfo = new HashMap<>();
                            speedInfo.put("StaticSpeedDistance", LM_STATIC_SPEED_DISTANCE);
                            speedInfo.put("SpeedClass", LM_STATIC_SPEED_CLASS);
                            speedInfo.put("SpeedType", Speed_Type);
                            speedInfo.put("SpeedA", speedA);
                            speedInfo.put("SpeedB", speedB);
                            speedInfo.put("SpeedC", speedC);
                            LM_SPEED_INFO.add(speedInfo);
                        }
                        String classifiedSpeedInfo = objectMapper.writeValueAsString(LM_SPEED_INFO);
                        trainRRi.setClassifiedSpeedInfo(classifiedSpeedInfo);
                        int paddingBitsSSB = 8 - (m % 8);

                        if (paddingBitsSSB < 8) {
                            m += paddingBitsSSB; // Add the padding bits to align to the byte boundary
                        }

                        String SUB_PACKET_2 = CommonFile.extractBitsAsString(data, m, 4);
                        m += 4;
                        if (SUB_PACKET_2.equals("0010")) {
                            trainRRi.setSubPktTypeGP("Gradient Profile");
                            trainRRi.setSubPktLenGP(CommonFile.extractBits(data, m, 7));
                            m += 7;
                            int LM_Grad_Info_CNT = CommonFile.extractBits(data, m, 5);
                            m += 5;
                            trainRRi.setGpCountInfo(LM_Grad_Info_CNT);

                            List<Map<String, Object>> gradientList = new ArrayList<>();

                            for (int i = 0; i < LM_Grad_Info_CNT; i++) {
                                // Extract and process the gradient distance
                                int LM_GRADIENT_DISTANCE = CommonFile.extractBits(data, m, 15);
                                m += 15;

                                // Extract and process the gradient direction
                                String LM_GDIR = CommonFile.extractBitsAsString(data, m, 1);
                                String Grd_Dir;
                                switch (LM_GDIR) {
                                    case "0":
                                        Grd_Dir = "DownHill";
                                        break;
                                    case "1":
                                        Grd_Dir = "UpHill";
                                        break;
                                    default:
                                        Grd_Dir = "Unknown";
                                        break;
                                }
                                m += 1;
                                int LM_GRADIENT_VALUE = CommonFile.extractBits(data, m, 5);
                                m += 5;
                                Map<String, Object> gradientInfo = new HashMap<>();
                                gradientInfo.put("GradientDistance", LM_GRADIENT_DISTANCE);
                                gradientInfo.put("GradientDirection", Grd_Dir);
                                gradientInfo.put("GradientValue", LM_GRADIENT_VALUE);
                                gradientList.add(gradientInfo);
                            }
                            String gradientInfo = objectMapper.writeValueAsString(gradientList);
                            trainRRi.setGradientInfo(gradientInfo);

                            int paddingBitsGP = 8 - (m % 8);
                            if (paddingBitsGP < 8) {
                                m += paddingBitsGP; // Add the padding bits to align to the byte boundary
                            }

                            String SUB_PACKET_3 = CommonFile.extractBitsAsString(data, m, 4);
                            m += 4;
                            if (SUB_PACKET_3.equals("0011")) {
                                trainRRi.setSubPktTypeLC("LC gate profile");
                                trainRRi.setSubPktLenLC(CommonFile.extractBits(data, m, 7));
                                m += 7;
                                int LM_LC_Info_CNT = CommonFile.extractBits(data, m, 5);
                                m += 5;
                                trainRRi.setLmCountInfo(LM_LC_Info_CNT);
                                List<Map<String, Object>> lcInfoList = new ArrayList<>();

                                for (int i = 0; i < LM_LC_Info_CNT; i++) {
                                    Map<String, Object> lcInfo = new HashMap<>();

                                    // Extract LC Distance
                                    int lmLcDistance = CommonFile.extractBits(data, m, 15);
                                    m += 15;
                                    lcInfo.put("Distance", lmLcDistance);

                                    // Extract and Decode LC ID Numeric
                                    int lmLcIdNumeric = CommonFile.extractBits(data, m, 10);
                                    String lcIdNumeric;
                                    if (lmLcIdNumeric == 0) {
                                        lcIdNumeric = "Invalid";
                                    } else if (lmLcIdNumeric == 1022) {
                                        lcIdNumeric = "LC Gate Number other than 1 to 1022 - out of range (Display xx on DMI)";
                                    } else if (lmLcIdNumeric == 1023) {
                                        lcIdNumeric = "Spare";
                                    } else if (lmLcIdNumeric >= 1 && lmLcIdNumeric <= 1021) {
                                        lcIdNumeric = "LC Gate Number: " + lmLcIdNumeric;
                                    } else {
                                        lcIdNumeric = "Unknown";
                                    }
                                    m += 10;
                                    lcInfo.put("IdNumeric", lcIdNumeric);

                                    // Extract and Decode LC Alpha Suffix
                                    String lmLcIdAlphaSuffix = CommonFile.extractBitsAsString(data, m, 3);
                                    String lcIdAlphaSuffix = switch (lmLcIdAlphaSuffix) {
                                        case "000" -> "No suffix";
                                        case "001" -> "a";
                                        case "010" -> "b";
                                        case "011" -> "c";
                                        case "100" -> "d";
                                        case "101" -> "e";
                                        case "110" -> "Out of Range";
                                        case "111" -> "Spare";
                                        default -> "Unknown";
                                    };
                                    m += 3;
                                    lcInfo.put("AlphaSuffix", lcIdAlphaSuffix);

                                    // Extract and Decode Manning Type
                                    String lmLcManningType = CommonFile.extractBitsAsString(data, m, 1);
                                    String mappingType = lmLcManningType.equals("0") ? "Manned"
                                            : lmLcManningType.equals("1") ? "Unmanned" : "Unknown";
                                    m += 1;
                                    lcInfo.put("ManningType", mappingType);

                                    // Extract and Decode Class
                                    String lmLcClass = CommonFile.extractBitsAsString(data, m, 3);
                                    String lcClass = switch (lmLcClass) {
                                        case "000" -> "Spl";
                                        case "001" -> "A";
                                        case "010" -> "B1";
                                        case "011" -> "B2";
                                        case "100" -> "B";
                                        case "101" -> "C";
                                        case "110" -> "D";
                                        case "111" -> "Spare";
                                        default -> "Unknown";
                                    };
                                    m += 3;
                                    lcInfo.put("Class", lcClass);

                                    // Extract and Decode Whistling Enabled
                                    String lmLcWhistlingEnabled = CommonFile.extractBitsAsString(data, m, 1);
                                    String autoWhistleEnabled = lmLcWhistlingEnabled.equals("0") ? "No"
                                            : lmLcWhistlingEnabled.equals("1") ? "Yes" : "Unknown";
                                    m += 1;
                                    lcInfo.put("WhistlingEnabled", autoWhistleEnabled);

                                    // Extract and Decode Whistling Type
                                    String lmLcWhistlingType = CommonFile.extractBitsAsString(data, m, 2);
                                    String autoWhistleType = switch (lmLcWhistlingType) {
                                        case "00" -> "Distance Based";
                                        case "01" -> "Time Based";
                                        case "10" -> "Configured Pattern Based";
                                        case "11" -> "Spare";
                                        default -> "Unknown";
                                    };
                                    m += 2;
                                    lcInfo.put("WhistlingType", autoWhistleType);
                                    lcInfoList.add(lcInfo);
                                }
                                String lcInfo = objectMapper.writeValueAsString(lcInfoList);
                                int paddingBitsLC = 8 - (m % 8);
                                if (paddingBitsLC < 8) {
                                    m += paddingBitsLC;
                                }
                                String SUB_PACKET_4 = CommonFile.extractBitsAsString(data, m, 4);
                                m += 4;
                                if (SUB_PACKET_4.equals("0100")) {
                                    trainRRi.setSubPktTypeTSP("TURN OUT SPEED PROFILE");
                                    trainRRi.setSubPktLenTSP(CommonFile.extractBits(data, m, 7));
                                    m += 7;
                                    int To_CNT = CommonFile.extractBits(data, m, 5);
                                    m += 5;
                                    trainRRi.setToCountInfo(To_CNT);
                                    List<Map<String, Object>> toSpeedInfoList = new ArrayList<>();

                                    for (int i = 0; i < To_CNT; i++) {
                                        // Extract TO_SPEED and map to description
                                        String toSpeed = CommonFile.extractBitsAsString(data, m, 5);
                                        m += 5;
                                        String speedDescription;

                                        switch (toSpeed) {
                                            case "00000":
                                                speedDescription = "Not Used";
                                                break;
                                            case "00001":
                                                speedDescription = "Up to 5 kmph";
                                                break;

                                            case "00010":
                                                speedDescription = "Up to 10 kmph";
                                                break;
                                            case "00011":
                                                speedDescription = "Up to 15 kmph";
                                                break;
                                            case "10010":
                                                speedDescription = "Up to 90 kmph";
                                                break;
                                            case "10011":
                                            case "10100":
                                            case "10101":
                                            case "10110":
                                            case "10111":
                                            case "11000":
                                            case "11001":
                                            case "11010":
                                            case "11011":
                                            case "11100":
                                            case "11101":
                                            case "11110":
                                                speedDescription = "Reserved for future use";
                                                break;
                                            case "11111":
                                                speedDescription = "Unrestricted";
                                                break;
                                            default:
                                                speedDescription = "Unknown";
                                                break;
                                        }

                                        // Extract DIFF_DIST_TO and TO_SPEED_REL_DIST
                                        int diffDistTo = CommonFile.extractBits(data, m, 15);
                                        m += 15;
                                        int toSpeedRelDist = CommonFile.extractBits(data, m, 12);
                                        m += 12;

                                        // Add data to list as a map
                                        Map<String, Object> toSpeedInfo = new HashMap<>();
                                        toSpeedInfo.put("ToSpeed", toSpeed);
                                        toSpeedInfo.put("SpeedDescription", speedDescription);
                                        toSpeedInfo.put("DiffDistTo", diffDistTo);
                                        toSpeedInfo.put("ToSpeedRelDist", toSpeedRelDist);

                                        toSpeedInfoList.add(toSpeedInfo);
                                    }
                                    String speedInfo = objectMapper.writeValueAsString(toSpeedInfoList);
                                    trainRRi.setSpeedInfo(speedInfo);
                                    int paddingBitsTSP = 8 - (m % 8);

                                    // If m is not aligned to a byte boundary, add the necessary padding
                                    if (paddingBitsTSP < 8) {
                                        m += paddingBitsTSP; // Add the padding bits to align to the byte boundary
                                    }

                                    String SUB_PACKET_5 = CommonFile.extractBitsAsString(data, m, 4);
                                    m += 4;
                                    if (SUB_PACKET_5.equals("0101")) {
                                        trainRRi.setSubPktTypeTag("Tag Link Information");
                                        trainRRi.setSubPktLenTag(CommonFile.extractBits(data, m, 7));
                                        m += 7;
                                        trainRRi.setDistDupTag(CommonFile.extractBits(data, m, 4));
                                        m += 4;
                                        int ROUTE_RFID_CNT = CommonFile.extractBits(data, m, 6);
                                        m += 6;
                                        trainRRi.setRouteRfidCount(ROUTE_RFID_CNT);
                                        List<Map<String, Object>> routeRfidInfoList = new ArrayList<>();
                                        for (int i = 0; i < ROUTE_RFID_CNT; i++) {
                                            int distNxtRfid = CommonFile.extractBits(data, m, 11);
                                            m += 11;
                                            int nxtRfidTagId = CommonFile.extractBits(data, m, 10);
                                            m += 10;
                                            int dupTagDir = CommonFile.extractBits(data, m, 1);
                                            m += 1;
                                            Map<String, Object> rfidInfo = new HashMap<>();
                                            rfidInfo.put("DistNxtRfid", distNxtRfid);
                                            rfidInfo.put("NxtRfidTagId", nxtRfidTagId);
                                            rfidInfo.put("DupTagDir", dupTagDir);
                                            routeRfidInfoList.add(rfidInfo);
                                        }
                                        String RfidInfoList = objectMapper.writeValueAsString(routeRfidInfoList);
                                        trainRRi.setRfidInfoList(RfidInfoList);

                                        // trainRRi.setRouteIdString(routeRfidInfoList);
                                        String ABS_LOC_RESET = CommonFile.extractBitsAsString(data, m, 1);
                                        trainRRi.setAbsLocReset(ABS_LOC_RESET);
                                        m = +1;
                                        if (ABS_LOC_RESET.equals("1")) {

                                            trainRRi
                                                    .setStartDistToLocReset(CommonFile.extractBits(data, m, 15));
                                            m += 15;
                                            int ADJ_LOCO_DIR = CommonFile.extractBits(data, m, 2);

                                            String direction;
                                            switch (ADJ_LOCO_DIR) {
                                                case 00:
                                                    direction = "Not Known";
                                                    break;
                                                case 01:
                                                    direction = "Nominal";
                                                    break;
                                                case 10:
                                                    direction = "Reverse";
                                                    break;
                                                case 11:
                                                    direction = "Deduce from Tags";
                                                    break;
                                                default:
                                                    direction = "Unknown";
                                            }
                                            trainRRi.setAdjLocoDir(direction);
                                            m += 2;
                                            int ABS_LOC_CORRECTION = CommonFile.extractBits(data, m, 23);
                                            trainRRi.setAbsLocCorrection(ABS_LOC_CORRECTION);
                                            m += 23;
                                        }
                                        int ADJ_LINE_CNT = CommonFile.extractBits(data, m, 3);
                                        trainRRi.setAdjLineCnt(ADJ_LINE_CNT);
                                        if (ADJ_LINE_CNT == 0) {

                                            trainRRi.setSelfTin(CommonFile.extractBits(data, m, 9));
                                            m += 9;

                                        } else if (ADJ_LINE_CNT >= 1 && ADJ_LINE_CNT <= 5) {

                                            List<Map<String, Object>> adjLineInfoList = new ArrayList<>();

                                            for (int i = 0; i < ADJ_LINE_CNT; i++) {
                                                int adjTin = CommonFile.extractBits(data, m, 9);
                                                m += 9;
                                                Map<String, Object> adjLineInfo = new HashMap<>();
                                                adjLineInfo.put("AdjTin", adjTin);
                                                adjLineInfoList.add(adjLineInfo);
                                            }
                                            String adjLineInfo = objectMapper.writeValueAsString(adjLineInfoList);
                                            trainRRi.setSelfTinList(adjLineInfo);

                                        } else if (ADJ_LINE_CNT == 6) {
                                            trainRRi.setSelfTinList("Reserved");

                                        } else if (ADJ_LINE_CNT == 7) {
                                            trainRRi.setSelfTinList("Unknown");

                                        } else {
                                            trainRRi.setSelfTinList("Out of range");
                                        }
                                        m += 3;
                                        int paddingBitsTli = 8 - (m % 8);
                                        if (paddingBitsTli < 8) {
                                            m += paddingBitsTli;
                                        }

                                        String SUB_PACKET_6 = CommonFile.extractBitsAsString(data, m, 4);
                                        m += 4;
                                        if (SUB_PACKET_6.equals("0110")) {
                                            trainRRi.setSubPktTypeTCP("Track Condition data");
                                            trainRRi
                                                    .setSubPktLenTCP(CommonFile.extractBits(data, m, 7));
                                            m = m + 7;
                                            int TRACK_COND_CNT = CommonFile.extractBits(data, m, 4);
                                            trainRRi.setTrackCondCnt(TRACK_COND_CNT);
                                            m += 4;
                                            List<Map<String, Object>> trackCondInfoList = new ArrayList<>();

                                            for (int i = 0; i < TRACK_COND_CNT; i++) {

                                                String TRACK_COND_TYPE = CommonFile.extractBitsAsString(data, m, 4);
                                                String TrackCondDescription;

                                                switch (TRACK_COND_TYPE) {
                                                    case "0000":
                                                        TrackCondDescription = "Not used";
                                                        break;
                                                    case "0001":
                                                        TrackCondDescription = "Dead Stop";
                                                        break;
                                                    case "0010":
                                                        TrackCondDescription = "Radio hole (MA is valid up-to Comm. fail timeout)";
                                                        break;
                                                    case "0011":
                                                        TrackCondDescription = "Non stopping area";
                                                        break;
                                                    case "0100":
                                                        TrackCondDescription = "Tunnel stopping area";
                                                        break;
                                                    case "0101":
                                                        TrackCondDescription = "Powerless section (Neutral section)";
                                                        break;
                                                    case "0110":
                                                        TrackCondDescription = "Sound horn";
                                                        break;
                                                    case "0111":
                                                        TrackCondDescription = "Reversing area";
                                                        break;
                                                    case "1000":
                                                        TrackCondDescription = "Fouling Mark location";
                                                        break;
                                                    case "1001":
                                                        TrackCondDescription = "KAVACH Territory Exit";
                                                        break;
                                                    case "1010":
                                                    case "1011":
                                                    case "1100":
                                                    case "1101":
                                                    case "1110":
                                                    case "1111":
                                                        TrackCondDescription = "Reserved for future use";
                                                        break;
                                                    default:
                                                        TrackCondDescription = "Unknown";
                                                        break;
                                                }
                                                m += 4;
                                                int START_DIST_TRACKCOND = CommonFile.extractBits(data, m, 15);
                                                m += 15;
                                                int LENGTH_TRACKCOND = CommonFile.extractBits(data, m, 15);
                                                m += 15;
                                                Map<String, Object> trackCondInfo = new HashMap<>();
                                                trackCondInfo.put("TrackCondType", TrackCondDescription);
                                                trackCondInfo.put("StartDistTrackCond", START_DIST_TRACKCOND);
                                                trackCondInfo.put("LengthTrackCond", LENGTH_TRACKCOND);
                                                trackCondInfoList.add(trackCondInfo);
                                            }
                                            String trackCondInfo = objectMapper
                                                    .writeValueAsString(trackCondInfoList);
                                            trainRRi.setTrackCondInfo(trackCondInfo);

                                            int paddingBitsTCI = 8 - (m % 8);

                                            if (paddingBitsTCI < 8) {
                                                m += paddingBitsTCI;
                                            }

                                            String SUB_PACKET_7 = CommonFile.extractBitsAsString(data, m, 4);
                                            m += 4;

                                            if (SUB_PACKET_7.equals("0111")) {
                                                trainRRi
                                                        .setSubPacketTSRP("Temporary Speed Restriction Profile");
                                                trainRRi
                                                        .setSubPacketLengthTSRP(CommonFile.extractBits(data, m, 7));
                                                m = m + 7;
                                                String TSR_STATUS = CommonFile.extractBitsAsString(data, m, 2);
                                                trainRRi.setTsrStatus(TSR_STATUS);
                                                m = m + 2;
                                                int TSR_Info_CNT = CommonFile.extractBits(data, m, 5);
                                                trainRRi.setTsrCount(TSR_Info_CNT);
                                                m = m + 5;

                                                List<Map<String, Object>> tsrInfoList = new ArrayList<>();

                                                for (int i = 0; i < TSR_Info_CNT; i++) {
                                                    Map<String, Object> tsrInfo = new HashMap<>();
                                                    int TSR_ID = CommonFile.extractBits(data, m, 8);
                                                    m += 8;
                                                    tsrInfo.put("TSR_ID", TSR_ID);

                                                    // Extract TSR_Distance
                                                    int TSR_Distance = CommonFile.extractBits(data, m, 15);
                                                    m += 15;
                                                    tsrInfo.put("TSR_Distance", TSR_Distance);

                                                    // Extract TSR_Length
                                                    int TSR_Length = CommonFile.extractBits(data, m, 15);
                                                    m += 15;
                                                    tsrInfo.put("TSR_Length", TSR_Length);

                                                    // Extract and decode TSR_Class
                                                    int TSR_Class = CommonFile.extractBits(data, m, 1);
                                                    m += 1;
                                                    String TsrClass = switch (TSR_Class) {
                                                        case 0 -> "Universal Speed";
                                                        case 1 -> "Classified Speed";
                                                        default -> "Unknown";
                                                    };
                                                    tsrInfo.put("TSR_Class", TsrClass);

                                                    // Process based on TSR_Class
                                                    if (TSR_Class == 0) {
                                                        int TSR_Universal_Speed = CommonFile.extractBits(data, m,
                                                                6);
                                                        m += 6;

                                                        String speedDescription;
                                                        if (TSR_Universal_Speed == 0) {
                                                            speedDescription = "Dead stop";
                                                        } else if (TSR_Universal_Speed >= 1
                                                                && TSR_Universal_Speed <= 40) {
                                                            int speedInKmph = TSR_Universal_Speed * 5;
                                                            speedDescription = speedInKmph + " kmph";
                                                        } else if (TSR_Universal_Speed >= 41
                                                                && TSR_Universal_Speed <= 61) {
                                                            speedDescription = "Reserved for future use";
                                                        } else if (TSR_Universal_Speed == 62) {
                                                            speedDescription = "8 kmph";
                                                        } else {
                                                            speedDescription = "Unknown";
                                                        }
                                                        tsrInfo.put("TSR_Universal_Speed", speedDescription);
                                                    } else {
                                                        int TSR_ClassA_Speed = CommonFile.extractBits(data, m, 6);
                                                        m += 6;
                                                        tsrInfo.put("TSR_ClassA_Speed", TSR_ClassA_Speed);

                                                        int TSR_ClassB_Speed = CommonFile.extractBits(data, m, 6);
                                                        m += 6;
                                                        tsrInfo.put("TSR_ClassB_Speed", TSR_ClassB_Speed);

                                                        int TSR_ClassC_Speed = CommonFile.extractBits(data, m, 6);
                                                        m += 6;
                                                        tsrInfo.put("TSR_ClassC_Speed", TSR_ClassC_Speed);
                                                    }
                                                    int TSR_Whistle = CommonFile.extractBits(data, m, 2);
                                                    m += 2;
                                                    String Whistle = switch (TSR_Whistle) {
                                                        case 0 -> "Universal Speed";
                                                        case 1 -> "Classified Speed";
                                                        default -> "Unknown";
                                                    };
                                                    tsrInfo.put("TSR_Whistle", Whistle);

                                                    tsrInfoList.add(tsrInfo);
                                                }
                                                String tsrInfo = objectMapper.writeValueAsString(tsrInfoList);
                                                trainRRi.setTsrInfo(tsrInfo);

                                                int paddingBitsTsr = 8 - (m % 8);

                                                if (paddingBitsTsr < 8) {
                                                    m += paddingBitsTsr;
                                                }
                                                offset = m/8;
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                break;
            case "0106":
                trainTakenOver = new TrainTakenOver();
                trainTakenOver.setPacketName("Train Taken Over");
                trainTakenOver.setMessageLength(messageLength);
                trainTakenOver.setMessageSequence(messageSequence);
                trainTakenOver.setStationaryKavachId(stationaryKavachId);
                trainTakenOver.setNmsSystemId(nmsSystemId);
                trainTakenOver.setSystemVersion(systemVersion);
                trainTakenOver.setDate(date);
                trainTakenOver.setTime(time);
                trainTakenOver.setSpecificProtocol(specificProtocol);
                trainTakenOver.setSenderIdentifier(CommonFile.byteArrayToInt(data, offset, 20));
                offset += 20;
                trainTakenOver.setReceiverIdentifier(CommonFile.byteArrayToInt(data, offset, 20));
                offset += 20;
                trainTakenOver.setPacketMessageLength(CommonFile.byteArrayToInt(data, offset, 2));
                offset += 2;
                trainTakenOver.setFrameNumber(CommonFile.byteArrayToInt(data, offset, 3));
                offset += 3;
                trainTakenOver.setPacketMessageSequence(CommonFile.byteArrayToInt(data, offset, 2));
                offset += 2;
                trainTakenOver.setBorderRfidTag(CommonFile.byteArrayToInt(data, offset, 2));
                offset += 2;
                trainTakenOver.setOnboardKavachIdentity(CommonFile.byteArrayToInt(data, offset, 3));
                offset += 3;
                trainTakenOver.setMacCode(CommonFile.bytesToHex(data, offset, 4));
                offset += 4;
                break;
            case "0107":
                trainHandoverCancellation = new TrainHandoverCancellation();
                trainHandoverCancellation.setPacketName("Train Handver Cancellation");
                trainHandoverCancellation.setMessageLength(messageLength);
                trainHandoverCancellation.setMessageSequence(messageSequence);
                trainHandoverCancellation.setStationaryKavachId(stationaryKavachId);
                trainHandoverCancellation.setNmsSystemId(nmsSystemId);
                trainHandoverCancellation.setSystemVersion(systemVersion);
                trainHandoverCancellation.setDate(date);
                trainHandoverCancellation.setTime(time);
                trainHandoverCancellation.setSpecificProtocol(specificProtocol);
                trainHandoverCancellation.setSenderIdentifier(CommonFile.byteArrayToInt(data, offset, 20));
                offset += 20;
                trainHandoverCancellation.setReceiverIdentifier(CommonFile.byteArrayToInt(data, offset, 20));
                offset += 20;
                trainHandoverCancellation.setPacketMessageLength(CommonFile.byteArrayToInt(data, offset, 2));
                offset += 2;
                trainHandoverCancellation.setFrameNumber(CommonFile.byteArrayToInt(data, offset, 3));
                offset += 3;
                trainHandoverCancellation.setMessageSequence(CommonFile.byteArrayToInt(data, offset, 2));
                offset += 2;
                trainHandoverCancellation.setBorderRfid(CommonFile.byteArrayToInt(data, offset, 2));
                offset += 2;
                trainHandoverCancellation.setOnboardKavachIdentity(CommonFile.byteArrayToInt(data, offset, 3));
                offset += 3;
                trainHandoverCancellation.setMacCode(CommonFile.bytesToHex(data, offset, 4));
                offset += 4;
                break;
            case "010A":
                tslRequest = new TSLRequest();
                tslRequest.setPacketName("TSL Request");
                tslRequest.setMessageLength(messageLength);
                tslRequest.setMessageSequence(messageSequence);
                tslRequest.setStationaryKavachId(stationaryKavachId);
                tslRequest.setNmsSystemId(nmsSystemId);
                tslRequest.setSystemVersion(systemVersion);
                tslRequest.setDate(date);
                tslRequest.setTime(time);
                tslRequest.setSpecificProtocol(specificProtocol);
                tslRequest.setSenderIdentifier(CommonFile.byteArrayToInt(data, offset, 20));
                offset += 20;
                tslRequest.setReceiverIdentifier(CommonFile.byteArrayToInt(data, offset, 20));
                offset += 20;
                tslRequest.setPacketMessageLength(CommonFile.byteArrayToInt(data, offset, 2));
                offset += 2;
                tslRequest.setFrameNumber(CommonFile.byteArrayToInt(data, offset, 3));
                offset += 3;
                tslRequest.setPacketMessageSequence(CommonFile.byteArrayToInt(data, offset, 2));
                offset += 2;
                tslRequest.setBorderRfidTag(CommonFile.byteArrayToInt(data, offset, 2));
                offset += 2;
                tslRequest.setOnboardKavachIdentity(CommonFile.byteArrayToInt(data, offset, 3));
                offset += 3;
                tslRequest.setTslRouteId(CommonFile.bytesToHex(data, offset, 2));
                offset += 2;
                tslRequest.setMacCode(CommonFile.bytesToHex(data, offset, 4));
                offset += 4;
                break;
            case "010B":
                tslAuthority = new TSLAuthority();
                tslAuthority.setPacketName("TSL Authority");
                tslAuthority.setMessageLength(messageLength);
                tslAuthority.setMessageSequence(messageSequence);
                tslAuthority.setStationaryKavachId(stationaryKavachId);
                tslAuthority.setNmsSystemId(nmsSystemId);
                tslAuthority.setSystemVersion(systemVersion);
                tslAuthority.setDate(date);
                tslAuthority.setTime(time);
                tslAuthority.setSpecificProtocol(specificProtocol);
                tslAuthority.setSenderIdentifier(CommonFile.byteArrayToInt(data, offset, 20));
                offset += 20;
                tslAuthority.setReceiverIdentifier(CommonFile.byteArrayToInt(data, offset, 20));
                offset += 20;
                tslAuthority.setPacketMessageLength(CommonFile.byteArrayToInt(data, offset, 2));
                offset += 2;
                tslAuthority.setFrameNumber(CommonFile.byteArrayToInt(data, offset, 3));
                offset += 3;
                tslAuthority.setPacketMessageSequence(CommonFile.byteArrayToInt(data, offset, 2));
                offset += 2;
                tslAuthority.setBorderRfidTag(CommonFile.byteArrayToInt(data, offset, 2));
                offset += 2;
                tslAuthority.setOnboardKavachIdentity(CommonFile.byteArrayToInt(data, offset, 3));
                offset += 3;
                tslAuthority.setTslRouteRequestReply(CommonFile.bytesToHex(data, offset, 1));
                offset += 1;
                tslAuthority.setMacCode(CommonFile.bytesToHex(data, offset, 4));
                offset += 4;
                break;
            case "010C":
                fieldElementsStatusRequest = new FieldElementsStatusRequest();
                fieldElementsStatusRequest.setPacketName("Field Elements Status Request");
                fieldElementsStatusRequest.setMessageLength(messageLength);
                fieldElementsStatusRequest.setMessageSequence(messageSequence);
                fieldElementsStatusRequest.setStationaryKavachId(stationaryKavachId);
                fieldElementsStatusRequest.setNmsSystemId(nmsSystemId);
                fieldElementsStatusRequest.setSystemVersion(systemVersion);
                fieldElementsStatusRequest.setDate(date);
                fieldElementsStatusRequest.setTime(time);
                fieldElementsStatusRequest.setSpecificProtocol(specificProtocol);
                fieldElementsStatusRequest.setSenderIdentifier(CommonFile.byteArrayToInt(data, offset, 20));
                offset += 20;
                fieldElementsStatusRequest.setReceiverIdentifier(CommonFile.byteArrayToInt(data, offset, 20));
                offset += 20;
                fieldElementsStatusRequest.setPacketMessageLength(CommonFile.byteArrayToInt(data, offset, 2));
                offset += 2;
                fieldElementsStatusRequest.setFrameNumber(CommonFile.byteArrayToInt(data, offset, 3));
                offset += 3;
                fieldElementsStatusRequest.setPacketMessageSequence(CommonFile.byteArrayToInt(data, offset, 2));
                offset += 2;
                fieldElementsStatusRequest.setMacCode(CommonFile.bytesToHex(data, offset, 4));
                offset += 4;
                break;
            case "010D":
                fieldElementsStatus = new FieldElementsStatus();
                fieldElementsStatus.setPacketName("Field Elements Status");
                fieldElementsStatus.setMessageLength(messageLength);
                fieldElementsStatus.setMessageSequence(messageSequence);
                fieldElementsStatus.setStationaryKavachId(stationaryKavachId);
                fieldElementsStatus.setNmsSystemId(nmsSystemId);
                fieldElementsStatus.setSystemVersion(systemVersion);
                fieldElementsStatus.setDate(date);
                fieldElementsStatus.setTime(time);
                fieldElementsStatus.setSpecificProtocol(specificProtocol);
                fieldElementsStatus.setSenderIdentifier(CommonFile.byteArrayToInt(data, offset, 20));
                offset += 20;
                fieldElementsStatus.setReceiverIdentifier(CommonFile.byteArrayToInt(data, offset, 20));
                offset += 20;
                fieldElementsStatus.setPacketMessageLength(CommonFile.byteArrayToInt(data, offset, 2));
                offset += 2;
                fieldElementsStatus.setFrameNumber(CommonFile.byteArrayToInt(data, offset, 3));
                offset += 3;
                fieldElementsStatus.setPacketMessageSequence(CommonFile.byteArrayToInt(data, offset, 2));
                offset += 2;
                int totalFieldElements = CommonFile.byteArrayToInt(data, offset, 2);
                fieldElementsStatus.setTotalFieldElements(totalFieldElements);
                offset += 2;
                int numberOfBytes = (totalFieldElements) / 8;
                byte[] allFieldElements = new byte[numberOfBytes];

                offset += numberOfBytes;
                List<String> relayStatuses = new ArrayList<>(totalFieldElements);

                for (int i = 0; i < totalFieldElements; i++) {
                    int byteIndex = i / 8;
                    int bitIndex = i % 8;
                    boolean isPickedUp = (allFieldElements[byteIndex] & (1 << bitIndex)) != 0;
                    String status = isPickedUp ? "Pickup" : "Drop";
                    relayStatuses.add("Name: " + status);
                }
                fieldElementsStatus.setAllFieldElementsList(relayStatuses);
                fieldElementsStatus.setMacCode(CommonFile.bytesToHex(data, offset, 4));
                offset += 4;
                break;
            case "010E":
                trainHandoverCancellationAcknowledgement = new TrainHandoverCancellationAcknowledgement();
                trainHandoverCancellationAcknowledgement.setPacketName("TSL Authority");
                trainHandoverCancellationAcknowledgement.setMessageLength(messageLength);
                trainHandoverCancellationAcknowledgement.setMessageSequence(messageSequence);
                trainHandoverCancellationAcknowledgement.setStationaryKavachId(stationaryKavachId);
                trainHandoverCancellationAcknowledgement.setNmsSystemId(nmsSystemId);
                trainHandoverCancellationAcknowledgement.setSystemVersion(systemVersion);
                trainHandoverCancellationAcknowledgement.setDate(date);
                trainHandoverCancellationAcknowledgement.setTime(time);
                trainHandoverCancellationAcknowledgement.setSpecificProtocol(specificProtocol);
                trainHandoverCancellationAcknowledgement.setSenderIdentifier(CommonFile.byteArrayToInt(data, offset, 20));
                offset += 20;
                trainHandoverCancellationAcknowledgement.setReceiverIdentifier(CommonFile.byteArrayToInt(data, offset, 20));
                offset += 20;
                trainHandoverCancellationAcknowledgement.setPacketMessageLength(CommonFile.byteArrayToInt(data, offset, 2));
                offset += 2;
                trainHandoverCancellationAcknowledgement.setFrameNumber(CommonFile.byteArrayToInt(data, offset, 3));
                offset += 3;
                trainHandoverCancellationAcknowledgement
                        .setPacketMessageSequence(CommonFile.byteArrayToInt(data, offset, 2));
                offset += 2;
                trainHandoverCancellationAcknowledgement.setBorderRfidTag(CommonFile.byteArrayToInt(data, offset, 2));
                offset += 2;
                trainHandoverCancellationAcknowledgement
                        .setOnboardKavachIdentity(CommonFile.byteArrayToInt(data, offset, 3));
                offset += 3;
                trainHandoverCancellationAcknowledgement.setMacCode(CommonFile.bytesToHex(data, offset, 4));
                offset += 4;
                break;
        }
        String crc = CommonFile.bytesToHex(data, offset, 4);
        offset += 4;
        if (pdiVersionCheckPsk != null) {
            pdiVersionCheckPsk.setCrc(crc);
            pdiVersionCheckPskToSskBatch.add(pdiVersionCheckPsk);
        }
        if (pdiVersionCheckSskToPsk != null) {
            pdiVersionCheckSskToPsk.setCrc(crc);
            pdiVersionCheckSskToPsksBaList.add(pdiVersionCheckSskToPsk);
        }
        if (heartBeatMessage != null) {
            heartBeatMessage.setCrc(crc);
            heartbeatBatch.add(heartBeatMessage);
        }
        if (trainHandoverRequest != null) {
            trainHandoverRequest.setCrc(crc);
            trainHandoverRequestBatch.add(trainHandoverRequest);
        }
        if (trainTakenOver != null) {
            trainTakenOver.setCrc(crc);
            trainTakenOverBatch.add(trainTakenOver);
        }
        if (trainHandoverCancellation != null) {
            trainHandoverCancellation.setCrc(crc);
            trainHandoverCancellationBatch.add(trainHandoverCancellation);
        }
        if (tslRequest != null) {
            tslRequest.setCrc(crc);
            tslRequestBatch.add(tslRequest);
        }
        if (tslAuthority != null) {
            tslAuthority.setCrc(crc);
            tslAuthorityBatch.add(tslAuthority);
        }
        if (fieldElementsStatusRequest != null) {
            fieldElementsStatusRequest.setCrc(crc);
            fieldElementsStatusRequestBatch.add(fieldElementsStatusRequest);
        }
        if (fieldElementsStatus != null) {
            fieldElementsStatus.setCrc(crc);
            fieldElementsStatusBatch.add(fieldElementsStatus);
        }
        if (trainHandoverCancellationAcknowledgement != null) {
            trainHandoverCancellationAcknowledgement.setCrc(crc);
            trainHandoverCancellationAcknowledgementBatch
            .add(trainHandoverCancellationAcknowledgement);
        }
        return offset;
    }


    
}
