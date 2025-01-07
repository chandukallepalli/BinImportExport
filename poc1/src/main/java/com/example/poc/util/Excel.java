package com.example.poc.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.core.io.ByteArrayResource;

import com.example.poc.entity.HeartBeatMessage;
import com.example.poc.entity.Message;
import com.example.poc.entity.PDIVersionCheckSskToPsk;
import com.example.poc.entity.PdiVersionCheckPskToSsk;

import org.apache.commons.io.output.ByteArrayOutputStream;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class Excel {

    
     public static ByteArrayResource createExcelFile(List<Message> messages) {
        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {

            // Create a sheet with the custom name
            Sheet sheet = workbook.createSheet("filtered_file");

            // Create a bold font style for headers
            Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerFont.setFontName("Verdana");
            headerFont.setFontHeightInPoints((short) 10); // Font size 10

            // Create the header cell style
            CellStyle headerCellStyle = workbook.createCellStyle();
            headerCellStyle.setFont(headerFont);

            // Create a regular font style for data
            Font dataFont = workbook.createFont();
            dataFont.setFontName("Verdana");
            dataFont.setFontHeightInPoints((short) 10); // Font size 10

            CellStyle dataCellStyle = workbook.createCellStyle();
            dataCellStyle.setFont(dataFont);

            // Create the header row with bold text
            Row headerRow = sheet.createRow(0);
            String[] headers = { "Date", "Time", "Station", "Seq Num", "NMS Id", "System Ver", "Pkt Type", "Firm",
                    "Module", "Fault Type", "Fault Msg", "CRC" };

            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(headerCellStyle); // Apply bold style to headers
            }

            // Fill data rows with the regular style
            int rowIdx = 1;
            for (Message message : messages) {
                Row row = sheet.createRow(rowIdx++);

                row.createCell(0).setCellValue(message.getMessageDate()); 
                row.createCell(1).setCellValue(message.getMessageTime()); 
                row.createCell(2).setCellValue(message.getKavachSubSystemId());
                row.createCell(3).setCellValue(message.getMessageSequence()); 
                row.createCell(4).setCellValue(message.getNmsSystemId());
                row.createCell(5).setCellValue(message.getSystemVersion()); 
                row.createCell(6).setCellValue("Kavach Faults"); 
                row.createCell(7).setCellValue(message.getFirmName()); 
                row.createCell(8).setCellValue(message.getModuleId());
                row.createCell(9).setCellValue(message.getFaultCodeType()); 
                row.createCell(10).setCellValue(message.getFaultMessage()); 
                row.createCell(11).setCellValue(message.getCrc()); 

                for (int i = 0; i < headers.length; i++) {
                    row.getCell(i).setCellStyle(dataCellStyle);
                }
            }
            for (int i = 0; i < headers.length; i++) {
                sheet.autoSizeColumn(i);
            }

            workbook.write(out);
            return new ByteArrayResource(out.toByteArray());
        } catch (IOException e) {
            throw new RuntimeException("Failed to generate Excel file", e);
        }
    }

    public static ByteArrayResource createExcelFileForHeartbeatAndMessageType14(List<HeartBeatMessage> records) {
try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
    Sheet sheet = workbook.createSheet("HeartBeat");

    // Create header font style
    Font headerFont = workbook.createFont();
    headerFont.setBold(true);
    headerFont.setFontName("Verdana");
    headerFont.setFontHeightInPoints((short) 10);

    // Create header cell style
    CellStyle headerCellStyle = workbook.createCellStyle();
    headerCellStyle.setFont(headerFont);

    // Create data font style
    Font dataFont = workbook.createFont();
    dataFont.setFontName("Verdana");
    dataFont.setFontHeightInPoints((short) 10);

    // Create data cell style
    CellStyle dataCellStyle = workbook.createCellStyle();
    dataCellStyle.setFont(dataFont);

    // Headers based on the combined data from HeartBeat and MessageType14 entities
    String[] headers = {
            "Date", "Time", "Station", "Seq Num", "NMS Id", "System Ver", "Pkt Type",
            "Sender SKAVACH Id", "Receiver SKAVACH Id", "Msg Len", "Frame Num",
            "Msg Seq Num", "MAC Code", "CRC"
    };

    // Add headers to the sheet
    Row headerRow = sheet.createRow(0);
    for (int i = 0; i < headers.length; i++) {
        Cell cell = headerRow.createCell(i);
        cell.setCellValue(headers[i]);
        cell.setCellStyle(headerCellStyle);
    }
    sheet.setAutoFilter(new CellRangeAddress(0, 0, 0, headers.length - 1));

    // Fill data rows
    int rowIdx = 1;
    for (HeartBeatMessage record : records) {
        Row row = sheet.createRow(rowIdx++);

        // Row row = sheet.createRow(rowIdx++);

        row.createCell(0)
                .setCellValue(record.getDate()); // Date
        row.createCell(1)
                .setCellValue(record.getTime()); // Time
        row.createCell(2)
                .setCellValue(record.getStationaryKavachId()); // Station
        row.createCell(3).setCellValue(record.getMessageSequence()); // Seq
                                                                                                        // Num
        row.createCell(4).setCellValue(record.getNmsSystemId()); // NMS
                                                                                                        // Id
        row.createCell(5).setCellValue(record.getSystemVersion()); // System
                                                                                                            // Ver
        row.createCell(6).setCellValue(record.getPacketName()); // Default value for Pkt Type
        row.createCell(7)
                .setCellValue(record.getPrimarySecondarySKavach());
                     // Sender SKAVACH Id
        row.createCell(8)
                .setCellValue(record.getSecondarySecondarySKavach());
                         // Receiver SKAVACH Id
        row.createCell(9)
                .setCellValue(record.getMessageLength()); // Msg Len
        row.createCell(10)
                .setCellValue(record.getFrameNumber()); // Result
                                                                                                              // PDI
                                                                                                              // Ver
        row.createCell(11).setCellValue(record.getPacketMessageSequence()); // PDI Ver
        row.createCell(12).setCellValue(record.getMacCode()); // MAC
                                                                                                                // Code
        row.createCell(13).setCellValue(record.getCrc()); // CRC
        // Apply the data style to all cells
        for (int i = 0; i < headers.length; i++) {
            row.getCell(i).setCellStyle(dataCellStyle);
        }
    }

    // Auto-size columns for better appearance
    for (int i = 0; i < headers.length; i++) {
        sheet.autoSizeColumn(i);
    }

    workbook.write(out);
    return new ByteArrayResource(out.toByteArray());
} catch (IOException e) {
    throw new RuntimeException("Failed to generate Excel file", e);
}
}

 public static ByteArrayResource createExcelFileForFilteredCommandPdi(List<PdiVersionCheckPskToSsk> records) {
        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Sheet sheet = workbook.createSheet("CommandPdi");

            // Create header font style
            Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerFont.setFontName("Verdana");
            headerFont.setFontHeightInPoints((short) 10);

            // Create header cell style
            CellStyle headerCellStyle = workbook.createCellStyle();
            headerCellStyle.setFont(headerFont);

            // Create data font style
            Font dataFont = workbook.createFont();
            dataFont.setFontName("Verdana");
            dataFont.setFontHeightInPoints((short) 10);

            // Create data cell style
            CellStyle dataCellStyle = workbook.createCellStyle();
            dataCellStyle.setFont(dataFont);

            // Default columns
            String[] headers = { "Date", "Time", "Station", "Seq Num", "NMS Id", "System Ver", "Pkt Type",
                    "Sender Kavach Id", "Receiver SKAVACH Id",
                    "Msg Len", "PDI Ver", "Primary Random Num", "CRC" };

            // Add filter-based columns dynamically
            // List<String> dynamicColumns = new ArrayList<>(filters.keySet());
            // List<String> columns = new ArrayList<>(Arrays.asList(defaultColumns));
            // columns.addAll(dynamicColumns); // Combine default and dynamic columns

            // Create header row
            Row headerRow = sheet.createRow(0);
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(headerCellStyle);
            }

            // Fill data rows
            int rowIdx = 1;
            for (PdiVersionCheckPskToSsk record : records) {
                Row row = sheet.createRow(rowIdx++);

                row.createCell(0)
                        .setCellValue(record.getDate()); // Date
                row.createCell(1)
                        .setCellValue(record.getTime()); // Time
                row.createCell(2)
                        .setCellValue(record.getStationaryKavachId()); // Station
                row.createCell(3).setCellValue(record.getMessageSequence()); // Seq
                                                                                                                // Num
                row.createCell(4).setCellValue(record.getNmsSystemId()); // NMS
                                                                                                                // Id
                row.createCell(5).setCellValue(record.getSystemVersion()); // System
                                                                                                                    // Ver
                row.createCell(6).setCellValue(record.getPacketName()); // Default value for Pkt Type
                row.createCell(7)
                        .setCellValue(record.getSenderIdentifier()); // Sender SKAVACH Id
                row.createCell(8)
                        .setCellValue(record.getReceiverIdentifier()); // Receiver SKAVACH Id
                row.createCell(9)
                        .setCellValue(record.getMessageLength()); // Msg Len
                row.createCell(10)
                        .setCellValue(record.getPdiVersion()); // Result PDI Ver
                row.createCell(11).setCellValue(record.getRp()); // PDI
                                                                                                                   // Ver
                row.createCell(12).setCellValue(record.getCrc()); // MAC
                                                                                                                // Code

            }

            // Auto-size columns for better appearance
            for (int i = 0; i < headers.length; i++) {
                sheet.autoSizeColumn(i);
            }
            workbook.write(out);
            return new ByteArrayResource(out.toByteArray());
        } catch (IOException e) {
            throw new RuntimeException("Failed to generate Excel file", e);
        }
    }

    public static ByteArrayResource createExcelFileForMessagePdi(List<PDIVersionCheckSskToPsk> record) {
        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {

            // Create a sheet with the custom name
            Sheet sheet = workbook.createSheet("MessagePdi");

            // Create a bold font style for headers
            Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerFont.setFontName("Verdana");
            headerFont.setFontHeightInPoints((short) 10); // Font size 10

            // Create the header cell style
            CellStyle headerCellStyle = workbook.createCellStyle();
            headerCellStyle.setFont(headerFont);

            // Create a regular font style for data
            Font dataFont = workbook.createFont();
            dataFont.setFontName("Verdana");
            dataFont.setFontHeightInPoints((short) 10); // Font size 10

            CellStyle dataCellStyle = workbook.createCellStyle();
            dataCellStyle.setFont(dataFont);

            // Create the header row with bold text
            Row headerRow = sheet.createRow(0);

            // Updated headers based on your requirement
            String[] headers = { "Date", "Time", "Station", "Seq Num", "NMS Id", "System Ver", "Pkt Type",
                    "Sender SKAVACH Id", "Receiver SKAVACH Id", "Msg Len","Result Pdi Ver", "PDI Ver", "Secondary Random Num","Mac Code", "CRC" };

            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(headerCellStyle); // Apply bold style to headers
            }

            // Fill data rows with the regular style
            int rowIdx = 1;
            for (PDIVersionCheckSskToPsk message : record) {
                Row row = sheet.createRow(rowIdx++);

                row.createCell(0)
                        .setCellValue(message.getDate()); // Date
                row.createCell(1)
                        .setCellValue(message.getTime()); // Time
                row.createCell(2)
                        .setCellValue(message.getStationaryKavachId()); // Station
                row.createCell(3)
                        .setCellValue(message.getMessageSequence()); // Seq Num
                row.createCell(4).setCellValue(message.getNmsSystemId()); // NMS
                                                                                                                  // Id
                row.createCell(5).setCellValue(message.getSystemVersion());
                row.createCell(6).setCellValue(message.getPacketName()); // System
                                                                                                                // Ver
                row.createCell(7).setCellValue(message.getSenderIdentifier()); // Default value for Pkt Type
                row.createCell(8)
                        .setCellValue(message.getReceiverIdentifier()); // Sender SKAVACH Id
                row.createCell(9)
                        .setCellValue(message.getMessageLength()); // Receiver SKAVACH Id
                row.createCell(10).setCellValue(message.getResultPdiVersionCheck()); // Msg
                                                                                                                        // Len
                row.createCell(11)
                        .setCellValue(message.getPdiVersionSsk()); // Result PDI Ver
                row.createCell(12).setCellValue(message.getRN());
                row.createCell(13).setCellValue(message.getMacCode()); // CRC
                row.createCell(14).setCellValue(message.getCrc());

                // Apply the data style to all cells
                for (int i = 0; i < headers.length; i++) {
                    row.getCell(i).setCellStyle(dataCellStyle);
                }
            }

            // Auto-size columns for better appearance
            for (int i = 0; i < headers.length; i++) {
                sheet.autoSizeColumn(i);
            }

            workbook.write(out);
            return new ByteArrayResource(out.toByteArray());
        } catch (IOException e) {
            throw new RuntimeException("Failed to generate Excel file", e);
        }
    }

}