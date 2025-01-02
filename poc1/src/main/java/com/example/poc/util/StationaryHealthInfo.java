package com.example.poc.util;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.poc.dto.EventDetails;
import com.example.poc.entity.Message17;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
@Service
public class StationaryHealthInfo {

    //  @Autowired
    // private MessageType17Repository messageType17Repository;

    private final ObjectMapper objectMapper = new ObjectMapper();

    public int decodeMessageType17(byte[] data, int startPosition, int offset, List<Message17> messageType17Batch) {
        try {
            // long startTime = System.currentTimeMillis();
            Message17 messageType17 = new Message17();
            messageType17.setMessageLength(CommonFile.byteArrayToInt(data, offset, 2));
            offset += 2;
            messageType17.setMessageSequence(CommonFile.byteArrayToInt(data, offset, 2));
            offset += 2;
            messageType17.setStationaryKavachId(CommonFile.byteArrayToInt(data, offset, 2));
            offset += 2;
            messageType17.setNmsSystemId(CommonFile.byteArrayToInt(data, offset, 2));
            offset += 2;
            messageType17.setSystemVersion(CommonFile.byteArrayToInt(data, offset, 1));
            offset += 1;
            messageType17.setMessageDate(CommonFile.decodeDate(data, offset));
            offset += 3;
            messageType17.setMessageTime(CommonFile.decodeTime(data, offset));
            offset += 3;
            messageType17.setFirmName("kernex");
            int eventCount = CommonFile.byteArrayToInt(data, offset, 1);
            messageType17.setEventCount(eventCount);
            offset += 1;

            List<EventDetails> eventDetailsList = new ArrayList<>();
            for (int i = 0; i < eventCount; i++) {
                int eventId = CommonFile.byteArrayToInt(data, offset, 2);
             
                offset += 2;

                if (eventId > 255) {
                    String eventName = "Unknown Event";
                    String eventDescription = "Unknown Event Description";
                    EventDetails eventDetails = new EventDetails(eventId, eventName, eventDescription);
                    eventDetailsList.add(eventDetails);
                    continue;
                }

                String eventName = "event name"; 
                String variableName = "variable name"; 
               

                String eventDescription = variableName; 
                EventDetails eventDetails = new EventDetails(eventId, eventName, eventDescription);
                eventDetailsList.add(eventDetails);
            } 
            String relayEventsJson = objectMapper.writeValueAsString(eventDetailsList);
            messageType17.setEventDetails(relayEventsJson);

            messageType17.setPacketName("Station Health");

            messageType17.setCrc(CommonFile.bytesToHex(data, offset, 4));
            offset += 4;
         
            // System.out.println("Decode end time::"+LocalDateTime.now());

            if (messageType17 != null) {
                messageType17Batch.add(messageType17);
            }
           
            
        } catch (JsonProcessingException e) {
            System.err.println("Error serializing event details to JSON: " + e.getMessage());
            e.printStackTrace();
        }
        return offset;
    }
    
}
