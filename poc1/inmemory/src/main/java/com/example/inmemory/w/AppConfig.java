package com.example.inmemory.w;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {
    @Bean
    public ExcelReader excelReader() {
        return new ExcelReader();
    }
}

