package com.example.inmemory.controller;



import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.example.inmemory.DTO.Student;
import com.example.inmemory.Storage.InMemoryStudentStore;
import com.example.inmemory.w.ExcelReader;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/students")
public class StudentController {
    private final InMemoryStudentStore studentStore;
    private final ExcelReader excelReader;

    public StudentController(InMemoryStudentStore studentStore, ExcelReader excelReader) {
        this.studentStore = studentStore;
        this.excelReader = excelReader;
    }

    @PostMapping("/upload")
    public String uploadExcel(@RequestParam("file") MultipartFile file) {
        try {
            List<Student> students = excelReader.readExcel(file.getInputStream());
            String key = UUID.randomUUID().toString();
            studentStore.storeData(key, students);
            return "Data stored with key: " + key;
        } catch (Exception e) {
            return "Failed to upload: " + e.getMessage();
        }
    }

    @GetMapping("/all")
    public List<Student> getAllStudents() {
        return studentStore.getAllData().values().stream()
                .flatMap(data -> ((List<Student>) data.data()).stream())
                .toList();
    }

    @PostMapping("/clean-up")
    public String cleanUpData() {
    studentStore.cleanUpExpiredData();
    return "Expired data cleaned up successfully";
}

}
