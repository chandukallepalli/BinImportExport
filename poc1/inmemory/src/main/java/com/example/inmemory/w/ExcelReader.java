package com.example.inmemory.w;





import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;

import com.example.inmemory.DTO.Student;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
@Component
public class ExcelReader {
    public List<Student> readExcel(InputStream inputStream) throws Exception {
        List<Student> students = new ArrayList<>();
        try (Workbook workbook = new XSSFWorkbook(inputStream)) {
            Sheet sheet = workbook.getSheetAt(0); // First sheet
            for (Row row : sheet) {
                if (row.getRowNum() == 0) continue; // Skip header row
                Student student = new Student(
                        row.getCell(0).toString(),
                        row.getCell(1).toString(),
                        row.getCell(2).toString(),
                        row.getCell(3).toString()
                );
                students.add(student);
            }
        }
        return students;
    }
}

