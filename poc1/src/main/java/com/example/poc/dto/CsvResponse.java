package com.example.poc.dto;

import java.util.List;

public class CsvResponse<T> {
    // private List<String> columnNames;
    private List<T> data;

    // Constructor
    public CsvResponse( List<T> data) {
        // this.columnNames = columnNames;
        this.data = data;
    }

    // Getters and Setters
    // public List<String> getColumnNames() {
    //     return columnNames;
    // }

    // public void setColumnNames(List<String> columnNames) {
    //     this.columnNames = columnNames;
    // }

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }
}
