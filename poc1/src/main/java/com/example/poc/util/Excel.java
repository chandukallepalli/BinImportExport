package com.example.poc.util;

import java.io.IOException;
import java.util.List;

import org.springframework.core.io.ByteArrayResource;

import com.example.poc.entity.Message;

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

}