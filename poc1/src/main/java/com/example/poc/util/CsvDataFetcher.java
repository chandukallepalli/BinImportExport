package com.example.poc.util;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class CsvDataFetcher {

    private static final String DIRECTORY_PATH = "D:\\poc"; // Directory containing your files
    private static final SimpleDateFormat PAYLOAD_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static final SimpleDateFormat CSV_DATE_FORMAT = new SimpleDateFormat("dd/MM/yy HH:mm:ss"); // Updated to dd/MM/yy format

    // This function reads CSV data, filters it based on date range, and formats messageDate and messageTime
    public List<String[]> fetchFilteredData(String fromDateStr, String toDateStr) throws ParseException, IOException {
        // Parse input dates
        Date fromDate = PAYLOAD_DATE_FORMAT.parse(fromDateStr);
        Date toDate = PAYLOAD_DATE_FORMAT.parse(toDateStr);

        System.out.println("From Date: " + fromDate);
        System.out.println("To Date: " + toDate);

        // List CSV files
        File dir = new File(DIRECTORY_PATH);
        File[] csvFiles = dir.listFiles((dir1, name) -> name.startsWith("message_Type_17_") && name.endsWith(".csv"));

        if (csvFiles == null || csvFiles.length == 0) {
            throw new FileNotFoundException("No CSV files found in the directory: " + DIRECTORY_PATH);
        }

        List<String[]> resultData = new ArrayList<>();

        // Process each CSV file
        for (File csvFile : csvFiles) {
            System.out.println("Processing file: " + csvFile.getName());
            resultData.addAll(filterCsvData(csvFile, fromDate, toDate));
        }

        return resultData;
    }

    // This method filters the data from CSV based on the date range and formats the messageDate and messageTime
    private List<String[]> filterCsvData(File csvFile, Date fromDate, Date toDate) throws IOException {
        List<String[]> filteredData = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
            String header = br.readLine(); // Read header row
            if (header == null) return filteredData; // Skip empty files

            String[] headers = header.split(","); // Assuming comma-separated values
            int dateIndex = Arrays.asList(headers).indexOf("messageDate");
            int timeIndex = Arrays.asList(headers).indexOf("messageTime");

            if (dateIndex == -1 || timeIndex == -1) {
                System.err.println("Required columns 'messageDate' or 'messageTime' not found in file: " + csvFile.getName());
                return filteredData;
            }

            String line;
            while ((line = br.readLine()) != null) {
                String[] columns = line.split(",");
                if (columns.length <= Math.max(dateIndex, timeIndex)) {
                    continue; // Skip malformed rows
                }

                try {
                    // Combine messageDate and messageTime into a single timestamp
                    String combinedTimestamp = columns[dateIndex].trim() + " " + columns[timeIndex].trim();
                    Date rowDate = CSV_DATE_FORMAT.parse(combinedTimestamp);

                    // System.out.println("Row Timestamp: " + rowDate); // Debug row timestamp

                    // Check if the row is within the date range
                    if (!rowDate.before(fromDate) && !rowDate.after(toDate)) {
                        filteredData.add(columns);
                    }
                } catch (ParseException e) {
                    System.err.println("Invalid date/time format in row: " + line);
                }
            }
        }

        System.out.println("Filtered rows from file " + csvFile.getName() + ": " + filteredData.size());
        return filteredData;
    }

    public static void main(String[] args) {
        CsvDataFetcher fetcher = new CsvDataFetcher();
        try {
            // Payload dates
            String fromDate = "2024-08-24 00:00:00";
            String toDate = "2024-08-24 00:01:12";

            // Fetch filtered data
            List<String[]> data = fetcher.fetchFilteredData(fromDate, toDate);

            // Print the filtered data
            if (data.isEmpty()) {
                System.out.println("No data found in the specified range.");
            } else {
                System.out.println("Filtered Data:");
                for (String[] row : data) {
                    System.out.println(Arrays.toString(row));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
