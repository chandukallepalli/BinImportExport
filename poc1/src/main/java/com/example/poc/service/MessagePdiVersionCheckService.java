package com.example.poc.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Service;

import com.example.poc.entity.HeartBeatMessage;
import com.example.poc.entity.PDIVersionCheckSskToPsk;
import com.example.poc.entity.PdiVersionCheckPskToSsk;
import com.example.poc.util.Excel;

@Service
public class MessagePdiVersionCheckService {

    @Autowired
    private MessageService service;

    
    public List<PDIVersionCheckSskToPsk> getMessagesByDateRange(String fromDate, String toDate, int page, int pageSize) {
        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat cacheFormat = new SimpleDateFormat("dd/MM/yy HH:mm:ss");
        List<PDIVersionCheckSskToPsk> result = new ArrayList<>();
        ConcurrentHashMap<String, PDIVersionCheckSskToPsk> messagePdiVeresions = service.getMessagePdiVerSionCheck();
    
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
            for (Map.Entry<String, PDIVersionCheckSskToPsk> entry : messagePdiVeresions.entrySet()) {
                PDIVersionCheckSskToPsk message = entry.getValue();
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
    
    private String normalizeMessageDateTime(PDIVersionCheckSskToPsk message) {
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

     public ByteArrayResource generateFullExcel(String fromDate, String toDate) {
        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat cacheFormat = new SimpleDateFormat("dd/MM/yy HH:mm:ss");
        List<PDIVersionCheckSskToPsk> result = new ArrayList<>();
        ConcurrentHashMap<String, PDIVersionCheckSskToPsk> PDIVersionCheckSskToPsks = service.getMessagePdiVerSionCheck();
    
        try {
            // Parse input dates
            Date fromDateTime = inputFormat.parse(fromDate);
            Date toDateTime = inputFormat.parse(toDate);
    
            // Validate date range
            if (fromDateTime.after(toDateTime)) {
                throw new IllegalArgumentException("From date must be before or equal to To date.");
            }
    
            // Filter messages within the date range
            for (PDIVersionCheckSskToPsk message : PDIVersionCheckSskToPsks.values()) {
                try {
                    // Normalize message date/time
                    String messageDateTimeStr = normalizeMessageDateTime(message);
                    Date messageDateTime = cacheFormat.parse(messageDateTimeStr);
    
                    // Check if message falls within the specified date range
                    if (!messageDateTime.before(fromDateTime) && !messageDateTime.after(toDateTime)) {
                        result.add(message);
                    }
                } catch (ParseException e) {
                    // Log error and skip the current message
                    System.err.println("Error parsing message date/time for message ID: " + message.getId() + " - " + e.getMessage());
                }
            }
    
            // Sort results by date/time
            result.sort((m1, m2) -> {
                try {
                    Date d1 = cacheFormat.parse(normalizeMessageDateTime(m1));
                    Date d2 = cacheFormat.parse(normalizeMessageDateTime(m2));
                    return d1.compareTo(d2);
                } catch (ParseException e) {
                    // Log error and maintain the current order
                    System.err.println("Error during date comparison: " + e.getMessage());
                    return 0;
                }
            });
    
            // Generate Excel file and return as ByteArrayResource
            return Excel.createExcelFileForMessagePdi(result);
            // return new ByteArrayResource(excelFileData);
    
        } catch (ParseException e) {
            throw new IllegalArgumentException("Invalid date format. Expected format: yyyy-MM-dd HH:mm:ss", e);
        } catch (Exception e) {
            throw new RuntimeException("Error generating report: " + e.getMessage(), e);
        }
    }
    
    
    
}
