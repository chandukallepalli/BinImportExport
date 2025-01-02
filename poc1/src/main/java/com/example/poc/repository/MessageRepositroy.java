package com.example.poc.repository;
import com.example.poc.entity.Message;
import com.example.poc.entity.Message17;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.*;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

// import org.springframework.web.bind.annotation.PostMapping;
// import org.springframework.web.bind.annotation.RequestBody;


//     public static void main(String[] args) {
//         try {
//             // Step 1: Load and parse the JSON file
//             ObjectMapper objectMapper = new ObjectMapper();
//             JsonNode rootNode = objectMapper.readTree(new File("D:\\poc\\data.json")); // Your JSON file

//             // Step 2: Write to CSV
//             writeJsonToCsv(rootNode, "output.csv");
//         } catch (IOException e) {
//             e.printStackTrace();
//         }
//     }

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.List;
import org.springframework.stereotype.Repository;
@Repository
public class MessageRepositroy {
     private final String baseFilePath = "D:\\poc\\src\\main\\resources\\templates\\message19.csv";

    public void writeJsonToCsv(List<Message> messages) {
        if (messages == null || messages.isEmpty()) {
            System.out.println("No data to write to CSV.");
            return;
        }

        String filePath = generateUniqueFilePath();

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            // Extract fields dynamically from the first Message object
            Field[] fields = Message.class.getDeclaredFields();

            // Write header row
            StringBuilder header = new StringBuilder();
            for (Field field : fields) {
                header.append(field.getName()).append(",");
            }
            writer.write(header.substring(0, header.length() - 1)); // Remove trailing comma
            writer.newLine();

            // Write data rows
            for (Message message : messages) {
                StringBuilder row = new StringBuilder();
                for (Field field : fields) {
                    field.setAccessible(true); // Access private fields
                    Object value = field.get(message);
                    row.append(value != null ? value.toString() : "").append(",");
                }
                writer.write(row.substring(0, row.length() - 1)); // Remove trailing comma
                writer.newLine();
            }

            System.out.println("CSV file generated successfully at: " + filePath);
        } catch (IOException e) {
            System.err.println("Error writing to CSV file: " + e.getMessage());
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            System.err.println("Error accessing fields of Message object: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private String generateUniqueFilePath() {
        // Add timestamp to the base file name to ensure uniqueness
        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        return baseFilePath + "_" + timestamp + ".csv";
    }
     private final Map<String, Message> cache = new ConcurrentHashMap<>();

    public int saveTrafficBatch(List<Message> binCacheList) {
        binCacheList.forEach(binCache -> {
            String key = generateKey(binCache);
            cache.put(key, binCache);
        });
        return cache.size();
    }
        // private final Map<Integer, Message> dataStore = new HashMap<>();

    private String generateKey(Message binCache) {
        return binCache.getMessageDate() + "-" + binCache.getMessageTime(); // Customize as needed
    }

         public List<Message> getAllMessages() {
        return new ArrayList<>(cache.values());
    }

    private final ConcurrentHashMap<String, StoredStudentData> dataStore = new ConcurrentHashMap<>();
       public void storeData(Object data) {

                    String key = UUID.randomUUID().toString();

        dataStore.put(key, new StoredStudentData(data, LocalDateTime.now()));
    }
    public record StoredStudentData(Object data, LocalDateTime createdTime) {
        boolean isExpired(LocalDateTime currentTime) {
            return createdTime.plusDays(2).isBefore(currentTime);
        }
    }

    public ConcurrentHashMap<String, StoredStudentData> getAllData() {
        return dataStore;
    }
    
}
