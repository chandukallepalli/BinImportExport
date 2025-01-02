package com.example.poc.util;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.example.poc.entity.Message;

@Component
public class KavachFaultMessageToNmsServer {

    // @Autowired
    // private MessageType19Repository messageType19Repository;

       public int decodeMessageType19(byte[] data, int startPosition, int n, List<Message> messageType19Batch) {
        Message messageType19 = new Message(null, null);
        AtomicInteger idCounter = new AtomicInteger(1);

        messageType19.setId(idCounter.getAndIncrement());

        messageType19.setMessageLength(CommonFile.byteArrayToInt(data, n, 2));
        n += 2;
        messageType19.setMessageSequence(CommonFile.byteArrayToInt(data, n, 2));
        n += 2;
        messageType19.setKavachSubSystemId(CommonFile.byteArrayToInt(data, n, 3));
        n += 3;
        messageType19.setNmsSystemId(CommonFile.byteArrayToInt(data, n, 2));
        n += 2;
        messageType19.setSystemVersion(CommonFile.byteArrayToInt(data, n, 1));
        n += 1;
        messageType19.setMessageDate(CommonFile.decodeDate(data, n));
        n += 3;
        messageType19.setMessageTime(CommonFile.decodeTime(data, n));
        n += 3;
        String KAVACHSubSystemType = CommonFile.bytesToHex(data, n, 1);

        String SubSystemDescription;

        switch (KAVACHSubSystemType) {
            case "11":
                SubSystemDescription = "Stationary KAVACH";
                break;
            case "22":
                SubSystemDescription = "Onboard KAVACH";
                break;
            case "33":
                SubSystemDescription = "TSRMS";
                break;
            default:
                SubSystemDescription = "Unknown KAVACH Subsystem Type";
                break;
        }
        messageType19.setKavachSubSystemType(SubSystemDescription);
        n += 1;
        messageType19.setTotalFaultCodes(CommonFile.byteArrayToInt(data, n, 1));
        n += 1;
        messageType19.setModuleId(CommonFile.byteArrayToInt(data, n, 1));
        n += 1;
        messageType19.setFirmName("Kernex");// firm id
        int faultCodeType = CommonFile.byteArrayToInt(data, n, 1);
        String Fault_Type;

        switch (faultCodeType) {
            case 1:
                Fault_Type = "Fault Code";
                break;
            case 2:
                Fault_Type = "Recovery Code";
                break;
            default:
                Fault_Type = "Unknown Fault Type";
                break;
        }
        messageType19.setFaultCodeType(Fault_Type);
        n += 1;
        messageType19.setFaultCode(CommonFile.byteArrayToInt(data, n, 2));
        n += 2;
        messageType19.setFaultMessage("Fault_Type");// stationFaultsService.getCachedFaultMessageType(KAVACHSubSystemType,
                                                    // faultCode);
        messageType19.setCrc(CommonFile.bytesToHex(data, n, 4));
        n += 4;
        if (messageType19 != null) {
            messageType19Batch.add(messageType19);
        }
        return n;
    }}
