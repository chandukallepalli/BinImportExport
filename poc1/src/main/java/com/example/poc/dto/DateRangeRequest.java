package com.example.poc.dto;

import java.util.List;
import java.util.Map;

public class DateRangeRequest {
    private String fromDate;
    private String toDate;
    private Map<String, List<Integer>> filters;

    // Getters and Setters
    public String getFromDate() {
        return fromDate;
    }

    public void setFromDate(String fromDate) {
        this.fromDate = fromDate;
    }

    public String getToDate() {
        return toDate;
    }

    public void setToDate(String toDate) {
        this.toDate = toDate;
    }

    public Map<String, List<Integer>> getFilters() {
        return filters;
    }

    public void setFilters(Map<String, List<Integer>> filters) {
        this.filters = filters;
    }}
