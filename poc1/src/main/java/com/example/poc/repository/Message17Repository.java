package com.example.poc.repository;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import com.example.poc.entity.Message17;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class Message17Repository {
    private static final Logger logger = LoggerFactory.getLogger(Message17Repository.class);
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yy/MM/dd HH:mm:ss");
    private static final long EXPIRATION_DAYS = 2;
    private final ConcurrentHashMap<String, StoredMessageData> dataStore = new ConcurrentHashMap<>();

    // Define supported filter mappings
    private static final Map<String, Function<StoredMessageData, List<?>>> FILTER_MAPPINGS = Map.of(
        "stationaryKavachId", data -> data.data().stream()
            .map(Message17::getStationaryKavachId)
            .collect(Collectors.toList()),
        "messageSequence", data -> data.data().stream()
            .map(Message17::getMessageSequence)
            .collect(Collectors.toList()),
        "nmsSystemId", data -> data.data().stream()
            .map(Message17::getNmsSystemId)
            .collect(Collectors.toList()),
        "firmName", data -> data.data().stream()
            .map(Message17::getFirmName)
            .collect(Collectors.toList())
    );

    public void storeData(List<Message17> data) {
        logger.debug("storeData() called with data: {}", data);
        if (data == null || data.isEmpty()) {
            logger.error("Data is null or empty");
            throw new IllegalArgumentException("Data is null or empty");
        }

        data.forEach(message -> {
            String key = createKey(message);
            logger.debug("Storing message with key: {}", key);
            dataStore.put(key, new StoredMessageData(
                List.of(message),
                LocalDateTime.now()
            ));
        });
        logger.info("Data successfully stored. Current dataStore size: {}", dataStore.size());
    }

    private String createKey(Message17 message) {
        String key = String.format("%s %s", message.getMessageDate(), message.getMessageTime());
        logger.debug("createKey() generated key: {}", key);
        return key;
    }

    public List<StoredMessageData> filterByDateTimeRange(String startDateStr, String endDateStr) {
        logger.debug("filterByDateTimeRange() called with startDateStr: {}, endDateStr: {}", startDateStr, endDateStr);
        validateDateTimeInput(startDateStr, endDateStr);

        List<StoredMessageData> filteredData = dataStore.entrySet().stream()
            .filter(entry -> isWithinRange(entry.getKey(), startDateStr, endDateStr))
            .map(Map.Entry::getValue)
            .collect(Collectors.toList());

        logger.info("filterByDateTimeRange() found {} records in range", filteredData.size());
        return filteredData;
    }

    private void validateDateTimeInput(String startDateStr, String endDateStr) {
        logger.debug("validateDateTimeInput() called with startDateStr: {}, endDateStr: {}", startDateStr, endDateStr);
        if (startDateStr == null || startDateStr.isEmpty() || 
            endDateStr == null || endDateStr.isEmpty()) {
            logger.error("Start date or end date is null or empty");
            throw new IllegalArgumentException("Start date or end date is null or empty");
        }

        try {
            LocalDateTime.parse(startDateStr, DATE_TIME_FORMATTER);
            LocalDateTime.parse(endDateStr, DATE_TIME_FORMATTER);
        } catch (DateTimeParseException e) {
            logger.error("Invalid date format. Expected format: yy/MM/dd HH:mm:ss", e);
            throw new IllegalArgumentException("Invalid date format. Expected format: yy/MM/dd HH:mm:ss", e);
        }
    }

    private boolean isWithinRange(String key, String startKey, String endKey) {
        logger.debug("isWithinRange() called with key: {}, startKey: {}, endKey: {}", key, startKey, endKey);
        boolean result = key.compareTo(startKey) >= 0 && key.compareTo(endKey) <= 0;
        logger.debug("isWithinRange() result: {}", result);
        return result;
    }

    public long countMessage17() {
        logger.debug("countMessage17() called");
        long count = dataStore.size();
        logger.info("countMessage17() count: {}", count);
        return count;
    }

    public List<Message17> getAllMessage17() {
        logger.debug("getAllMessage17() called");
        List<Message17> messages = dataStore.values()
            .stream()
            .flatMap(record -> record.data().stream())
            .collect(Collectors.toList());
        logger.info("getAllMessage17() retrieved {} messages", messages.size());
        return messages;
    }

    public Optional<Message17> getOneMessage17() {
        logger.debug("getOneMessage17() called");
        Optional<Message17> message = dataStore.values()
            .stream()
            .flatMap(record -> record.data().stream())
            .findFirst();
        logger.info("getOneMessage17() result: {}", message.orElse(null));
        return message;
    }

    public record StoredMessageData(List<Message17> data, LocalDateTime createdTime) {
        public StoredMessageData {
            data = Collections.unmodifiableList(data);
        }
    }

    public List<StoredMessageData> filterData(String fromDate, String toDate, int pageSize, int page, Map<String, List<String>> filters) {
        logger.debug("filterData() called with fromDate: {}, toDate: {}, pageSize: {}, page: {}, filters: {}", 
                     fromDate, toDate, pageSize, page, filters);

        validateDateTimeInput(fromDate, toDate);
        validatePagination(page, pageSize);

        List<StoredMessageData> filteredData = dataStore.entrySet().stream()
            .filter(entry -> isWithinRange(entry.getKey(), fromDate, toDate))
            .map(Map.Entry::getValue)
            .filter(data -> matchesFilters(data, filters))
            .skip((long) page * pageSize)
            .limit(pageSize)
            .collect(Collectors.toList());

        logger.info("filterData() found {} records after filtering", filteredData.size());
        return filteredData;
    }

    private void validatePagination(int page, int size) {
        logger.debug("validatePagination() called with page: {}, size: {}", page, size);
        if (page < 0 || size <= 0) {
            logger.error("Invalid pagination parameters: page must be >= 0 and size must be > 0");
            throw new IllegalArgumentException("Invalid pagination parameters: page must be >= 0 and size must be > 0");
        }
    }

    private boolean matchesFilters(StoredMessageData data, Map<String, List<String>> filters) {
        if (filters == null || filters.isEmpty()) {
            return true;
        }

        return filters.entrySet().stream()
            .allMatch(entry -> {
                String fieldName = entry.getKey();
                List<String> filterValues = entry.getValue();

                if (FILTER_MAPPINGS.containsKey(fieldName)) {
                    List<?> fieldValues = FILTER_MAPPINGS.get(fieldName).apply(data);
                    return fieldValues.stream()
                        .anyMatch(value -> filterValues.contains(String.valueOf(value)));
                }

                logger.warn("Unsupported filter field: {}", fieldName);
                return false;
            });
    }

    private boolean isWithinRange(String key, LocalDateTime startDate, LocalDateTime endDate) {
        try {
            LocalDateTime messageDateTime = LocalDateTime.parse(key, DATE_TIME_FORMATTER);
            return !messageDateTime.isBefore(startDate) && !messageDateTime.isAfter(endDate);
        } catch (DateTimeParseException e) {
            logger.error("Error parsing date from key: {}", key, e);
            return false;
        }
    }


    public ByteArrayResource generateFullExcel(String startDateStr, String endDateStr) {
        logger.info("generateFullExcel() called with startDateStr: {}, endDateStr: {}", startDateStr, endDateStr);
        validateDateTimeInput(startDateStr, endDateStr);

        List<StoredMessageData> filteredData = dataStore.entrySet().stream()
            .filter(entry -> isWithinRange(entry.getKey(), startDateStr, endDateStr))
            .map(Map.Entry::getValue)
            .collect(Collectors.toList());
    
        logger.info("generateFullExcel() found {} records within the range", filteredData.size());
    
        // Generate and return the Excel file
        return createExcelWithAllColumns(filteredData);
    }
    
    private static ByteArrayResource createExcelWithAllColumns(List<StoredMessageData> storedMessages) {
        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
    
            // Create a sheet in the workbook
            Sheet sheet = workbook.createSheet("Messages");
    
            // Create styles for headers and data
            Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerFont.setFontName("Verdana");
            headerFont.setFontHeightInPoints((short) 10);
    
            CellStyle headerCellStyle = workbook.createCellStyle();
            headerCellStyle.setFont(headerFont);
    
            Font dataFont = workbook.createFont();
            dataFont.setFontName("Verdana");
            dataFont.setFontHeightInPoints((short) 10);
    
            CellStyle dataCellStyle = workbook.createCellStyle();
            dataCellStyle.setFont(dataFont);
    
            // Create the header row
            Row headerRow = sheet.createRow(0);
            String[] headers = {
                "Date", "Time", "Station", "Seq Num", "NMS Id", "System Ver",
                "Station Health", "Firm", "Event Name", "Event Info", "CRC"
            };
    
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(headerCellStyle);
            }
            sheet.setAutoFilter(new CellRangeAddress(0, 0, 0, headers.length - 1));
    
            // Fill data rows
            int rowIdx = 1;
            ObjectMapper objectMapper = new ObjectMapper();
    
            for (StoredMessageData storedMessage : storedMessages) {
                for (Message17 message : storedMessage.data()) {
                    try {
                        // Parse JSON event details if available
                        String eventDetailsJson = message.getEventDetails();
                        List<Map<String, Object>> eventDetails = objectMapper.readValue(
                            eventDetailsJson,
                            new TypeReference<List<Map<String, Object>>>() {}
                        );
    
                        for (Map<String, Object> event : eventDetails) {
                            Row row = sheet.createRow(rowIdx++);
                            String eventName = (String) event.get("eventName");
                            String eventDescription = (String) event.get("eventDescription");
    
                            row.createCell(0).setCellValue(message.getMessageDate());
                            row.createCell(1).setCellValue(message.getMessageTime());
                            row.createCell(2).setCellValue(message.getStationaryKavachId());
                            row.createCell(3).setCellValue(message.getMessageSequence());
                            row.createCell(4).setCellValue(message.getNmsSystemId());
                            row.createCell(5).setCellValue(message.getSystemVersion());
                            row.createCell(6).setCellValue(message.getPacketName());
                            row.createCell(7).setCellValue(message.getFirmName());
                            row.createCell(8).setCellValue(eventName);
                            row.createCell(9).setCellValue(eventDescription);
                            row.createCell(10).setCellValue(message.getCrc());
    
                            // Apply data cell style
                            for (int j = 0; j < headers.length; j++) {
                                row.getCell(j).setCellStyle(dataCellStyle);
                            }
                        }
                    } catch (IOException e) {
                        logger.error("Failed to parse event details JSON for message: {}", message, e);
                    }
                }
            }
    
            // Adjust column widths
            for (int i = 0; i < headers.length; i++) {
                sheet.autoSizeColumn(i);
            }
    
            // Write workbook to output stream
            workbook.write(out);
            return new ByteArrayResource(out.toByteArray());
        } catch (IOException e) {
            logger.error("Failed to create Excel file", e);
            throw new RuntimeException("Failed to generate Excel file", e);
        }
    }
    
}