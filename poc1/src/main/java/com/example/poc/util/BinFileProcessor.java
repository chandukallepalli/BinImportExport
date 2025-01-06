package com.example.poc.util;

import java.io.*;
import java.nio.file.*;

public class BinFileProcessor {

    public static void main(String[] args) {
        // Paths for input and output files
        String inputFilePath = "C:\\Users\\Admin\\Desktop\\BINFiles\\STN_ADJ_2024-08-24.bin"; // Replace with your source bin file path
        String outputFilePath = "C:\\Users\\Admin\\Desktop\\BINFiles\\bluepaltarget.bin"; // Replace with your target bin file path

        try {
           
            byte[] inputData = Files.readAllBytes(Paths.get(inputFilePath));
            byte[] outputData = new byte[inputData.length * 2];
            System.arraycopy(inputData, 0, outputData, 0, inputData.length);
            System.arraycopy(inputData, 0, outputData, inputData.length, inputData.length);

            // Write the modified data to the output binary file
            Files.write(Paths.get(outputFilePath), outputData);

            System.out.println("Data successfully processed and written to " + outputFilePath);
        } catch (IOException e) {
            System.err.println("Error processing the binary files: " + e.getMessage());
        }
    }
}

