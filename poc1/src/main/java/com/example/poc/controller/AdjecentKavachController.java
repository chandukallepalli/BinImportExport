package com.example.poc.controller;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.poc.dto.MessageSearchRequest;
import com.example.poc.entity.HeartBeatMessage;
import com.example.poc.entity.Message17;
import com.example.poc.service.CommandPdiVersionService;
import com.example.poc.service.HeartBeatMessageService;
import com.example.poc.service.MessagePdiVersionCheckService;

@Controller
@RequestMapping("/v1/adjacentkavach")
@CrossOrigin(origins = { "http://localhost:3000", "http://nms.bluepalapps.com" })
public class AdjecentKavachController {

    @Autowired
    private HeartBeatMessageService service;
    @Autowired
    private MessagePdiVersionCheckService messagePdiVersionCheckService;

    @Autowired
    private CommandPdiVersionService commandPdiVeresions;

    @PostMapping("/report")
    public ResponseEntity<Map<String, Object>> generateReport(
            @RequestBody MessageSearchRequest request,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDirection) {
    
        try {
            // Parse input parameters
            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            inputFormat.setLenient(false);
    
            String fromDate = request.getFromDate();
            String toDate = request.getToDate();
            String reportType = request.getReportType();
            int page = request.getPage();
            int pageSize = request.getPageSize();
            Map<String, List<Integer>> filters = request.getFilters();
    
            // Validate date range
            if (fromDate == null || toDate == null) {
                throw new IllegalArgumentException("FromDate and ToDate must be provided.");
            }
    
            // Prepare response data
            List<?> result = new ArrayList<>();
            Map<String, Object> response = new HashMap<>();
    
            // Handle "HeartBeat" report type
            if ("HeartBeat".equalsIgnoreCase(reportType)) {
                if (filters == null || filters.isEmpty()) {
                    // No filters provided
                    result = service.getMessagesByDateRange(fromDate, toDate, page, pageSize);
                } else {
                    // Apply filters if provided
                    // result = service.getMessagesByFilters(fromDate, toDate, filters, page, pageSize, sortBy, sortDirection);
                }
            } 
            // Handle "Message PDI" report type
            else if ("Message PDI".equalsIgnoreCase(reportType)) {
                if (filters == null || filters.isEmpty()) {
                    result = messagePdiVersionCheckService.getMessagesByDateRange(fromDate, toDate, page, pageSize);
                } else {
                    // Apply filters if provided
                    // result = messagePdiVersionCheckService.getMessagesByFilters(fromDate, toDate, filters, page, pageSize, sortBy, sortDirection);
                }
            }
            // Handle "Command PDI" report type
            else if ("Command PDI".equalsIgnoreCase(reportType)) {
                if (filters == null || filters.isEmpty()) {
                    result = commandPdiVeresions.getMessagesByDateRange(fromDate, toDate, page, pageSize);
                } else {
                    // Apply filters if provided
                    // result = commandPdiVersionCheckService.getMessagesByFilters(fromDate, toDate, filters, page, pageSize, sortBy, sortDirection);
                }
            }
            
       
            else {
                throw new IllegalArgumentException("Unsupported report type: " + reportType);
            }
    
            // Build the response
            response.put("messages", result);
            response.put("totalCount", result.size());
            response.put("page", page);
            response.put("pageSize", pageSize);
    
            return ResponseEntity.ok(response);
    
        } catch (IllegalArgumentException e) {
            // Handle user input errors
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "Invalid request: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        } catch (Exception e) {
            // Handle unexpected errors
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "Error generating report: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }


    @PostMapping("/saveAll")
    public ResponseEntity<?> downloadAllColumns(@RequestBody  MessageSearchRequest request) {
        try {
            String reportType = request.getReportType();
            String fromDate = request.getFromDate();
            String toDate = request.getToDate();

            if (reportType == null || fromDate == null || toDate == null) {
                return ResponseEntity.badRequest().body("Missing required fields: reportType, fromDate, or toDate");
            }

            ByteArrayResource resource = generateReport(reportType, fromDate, toDate);

            if (resource == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Report generation failed or report type is not supported.");
            }

            String filename = "full_report_" + reportType.replace(" ", "_") + "_" + System.currentTimeMillis() + ".xlsx";
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename)
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .contentLength(resource.contentLength())
                    .body(resource);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Invalid input: " + e.getMessage());
        } catch (Exception e) {
            // Log the exception for debugging purposes
            e.printStackTrace(); // Use a logger in production code
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An unexpected error occurred: " + e.getMessage());
        }
    }

    private ByteArrayResource generateReport(String reportType, String fromDate, String toDate) {
        switch (reportType) {
            case "HeartBeat":
                return service.DownloadFullReport(fromDate, toDate);
            // Uncomment and implement these cases as needed
            case "Command PDI":
                return commandPdiVeresions.generateFullExcel(fromDate, toDate);
            case "Message PDI":
                return messagePdiVersionCheckService.generateFullExcel(fromDate, toDate);
            // case "Turnout Speed Profile":
            //     return trainRRITOUTService.generateFullExcel(fromDate, toDate);
            // case "Static Speed Profile":
            //     return trainRRITOUTService.generateFullExcel(fromDate, toDate);
            // case "Track Condition Data":
            //     return trainRRITrackON.generateFullExcel(fromDate, toDate);
            default:
                return null; // Unsupported report type
        }
    }
}

