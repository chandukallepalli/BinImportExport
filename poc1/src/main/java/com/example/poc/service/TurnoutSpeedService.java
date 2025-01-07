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

import com.example.poc.entity.TrainRRi;

@Service
public class TurnoutSpeedService {
     @Autowired
    private MessageService service;

     public List<TrainRRi> getMessagesByDateRange(String fromDate, String toDate, int page, int pageSize) {
        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat cacheFormat = new SimpleDateFormat("dd/MM/yy HH:mm:ss");
        List<TrainRRi> result = new ArrayList<>();
        ConcurrentHashMap<String, TrainRRi> commandPdiVeresions = service.getTrainRRi();
    
        try {
            // Parse input dates
            Date fromDateTime = inputFormat.parse(fromDate);
            Date toDateTime = inputFormat.parse(toDate);
    
            // Validate dates
            if (fromDateTime.after(toDateTime)) {
                throw new IllegalArgumentException("From date must be before or equal to the to date");
            }
    
            // Validate pagination parameters
            if (page < 0) {
                throw new IllegalArgumentException("Page number must be non-negative");
            }
            if (pageSize <= 0) {
                throw new IllegalArgumentException("Page size must be positive");
            }
    
            // Filter messages in the date range
            for (Map.Entry<String, TrainRRi> entry : commandPdiVeresions.entrySet()) {
                TrainRRi message = entry.getValue();
                try {
                    // Normalize the message date/time
                    String messageDateTimeStr = normalizeMessageDateTime(message);
                    Date messageDateTime = cacheFormat.parse(messageDateTimeStr);
    
                    // Check if the message is within the date range
                    if (!messageDateTime.before(fromDateTime) && !messageDateTime.after(toDateTime)) {
                        result.add(message);
                    }
                } catch (ParseException e) {
                    // Handle date parsing errors gracefully
                    // logger.error("Error parsing message date/time: {} {} - {}", message.getDate(), message.getTime(), e.getMessage());
                    continue; // Skip this message and continue with the next
                }
            }
    
            // Sort results by date/time
            result.sort((m1, m2) -> {
                try {
                    Date d1 = cacheFormat.parse(normalizeMessageDateTime(m1));
                    Date d2 = cacheFormat.parse(normalizeMessageDateTime(m2));
                    return d1.compareTo(d2);
                } catch (ParseException e) {
                    // Handle comparison errors gracefully
                    // logger.error("Error during date comparison: {}", e.getMessage());
                    return 0; // If there's an error in comparison, keep the order unchanged
                }
            });
    
            // Apply pagination
            int totalMessages = result.size();
            int startIndex = page * pageSize;
            int endIndex = Math.min(startIndex + pageSize, totalMessages);
    
            // If the requested page is out of bounds, return an empty list
            if (startIndex >= totalMessages) {
                return new ArrayList<>();
            }
    
            return result.subList(startIndex, endIndex);
    
        } catch (ParseException e) {
            // Log or handle the error with the input dates
            // logger.error("Error parsing input dates: {}", e.getMessage());
            throw new IllegalArgumentException("Invalid date format. Expected format: yyyy-MM-dd HH:mm:ss", e);
        }
    }
    
    private String normalizeMessageDateTime(TrainRRi message) {
        String dateStr = message.getDate();
        String timeStr = message.getTime();
    
        // Handle two-digit year format (e.g., "dd/MM/yy")
        if (dateStr.length() == 8) { // "dd/MM/yy"
            String[] dateParts = dateStr.split("/");
            if (dateParts.length == 3) {
                int year = Integer.parseInt(dateParts[2]);
                // Adjust year for 2000-2049 range (assuming years 00-49 are 2000-2049)
                if (year < 50) {
                    dateParts[2] = String.format("%02d", year + 2000); // Ensure the full year is represented
                }
                // Reconstruct the date string
                dateStr = String.format("%s/%s/%s", dateParts[0], dateParts[1], dateParts[2]);
            }
        }
    
        // Return normalized date-time string
        return dateStr + " " + timeStr;
    }

    
}
