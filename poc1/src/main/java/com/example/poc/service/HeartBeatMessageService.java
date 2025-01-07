package com.example.poc.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.poc.entity.HeartBeatMessage;

@Service
public class HeartBeatMessageService {
    @Autowired
    private MessageService service;

    public List<HeartBeatMessage> getMessagesByDateRange(String fromDate, String toDate, int page, int pageSize) {
        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat cacheFormat = new SimpleDateFormat("dd/MM/yy HH:mm:ss");
        List<HeartBeatMessage> result = new ArrayList<>();
        ConcurrentHashMap<String, HeartBeatMessage> heartBeatMessages = service.getHeartBeatMessages();

        try {
            // Parse input dates
            Date fromDateTime = inputFormat.parse(fromDate);
            Date toDateTime = inputFormat.parse(toDate);

            // Validate dates
            if (fromDateTime.after(toDateTime)) {
                throw new IllegalArgumentException("From date must be before or equal to to date");
            }

            // Validate pagination parameters
            if (page < 0) {
                throw new IllegalArgumentException("Page number must be non-negative");
            }
            if (pageSize <= 0) {
                throw new IllegalArgumentException("Page size must be positive");
            }

            // Filter messages in the date range
            for (Map.Entry<String, HeartBeatMessage> entry : heartBeatMessages.entrySet()) {
                HeartBeatMessage message = entry.getValue();
                try {
                    // Normalize the message date/time
                    String messageDateTimeStr = normalizeMessageDateTime(message);
                    Date messageDateTime = cacheFormat.parse(messageDateTimeStr);

                    // Check if message is within date range
                    if (!messageDateTime.before(fromDateTime) && !messageDateTime.after(toDateTime)) {
                        result.add(message);
                    }
                } catch (ParseException e) {
                    // logger.error("Error parsing message date/time: {} {} - {}",
                    // message.getDate(), message.getTime(), e.getMessage());
                    continue;
                }
            }

            // Sort results by date/time
            result.sort((m1, m2) -> {
                try {
                    Date d1 = cacheFormat.parse(normalizeMessageDateTime(m1));
                    Date d2 = cacheFormat.parse(normalizeMessageDateTime(m2));
                    return d1.compareTo(d2);
                } catch (ParseException e) {
                    // logger.error("Error during date comparison: {}", e.getMessage());
                    return 0;
                }
            });

            // Apply pagination
            int totalMessages = result.size();
            int startIndex = page * pageSize;
            int endIndex = Math.min(startIndex + pageSize, totalMessages);

            // Check if the requested page is valid
            if (startIndex >= totalMessages) {
                return new ArrayList<>(); // Return empty list if page is out of bounds
            }

            return result.subList(startIndex, endIndex);

        } catch (ParseException e) {
            // logger.error("Error parsing input dates: {}", e.getMessage());
            throw new IllegalArgumentException("Invalid date format. Expected format: yyyy-MM-dd HH:mm:ss", e);
        }
    }

    private String normalizeMessageDateTime(HeartBeatMessage message) {
        String dateStr = message.getDate();
        String timeStr = message.getTime();

        // Handle two-digit year format
        if (dateStr.length() == 8) { // dd/MM/yy
            String[] dateParts = dateStr.split("/");
            if (dateParts.length == 3) {
                int year = Integer.parseInt(dateParts[2]);
                // Adjust year for 2000-2049 range
                if (year < 50) {
                    dateParts[2] = String.format("%02d", year);
                }
                dateStr = String.format("%s/%s/%s", dateParts[0], dateParts[1], dateParts[2]);
            }
        }

        return dateStr + " " + timeStr;
    }

}
