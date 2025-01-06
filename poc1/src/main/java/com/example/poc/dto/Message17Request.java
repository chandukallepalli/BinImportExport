package com.example.poc.dto;

import java.util.List;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Message17Request {
    private String id;
    private String fromDate;
    private String toDate;
    private int page;
    private int pageSize;
    private Map<String, List<Integer>> filters;

   

   }
