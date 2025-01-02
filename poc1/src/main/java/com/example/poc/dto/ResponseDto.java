package com.example.poc.dto;

import java.util.List;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ResponseDto {
    private String fromDate;
    private String toDate;
    private int pageSize;
    private int page;
    private Map<String,List<String>> filters;

    
}
