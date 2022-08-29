package com.example.intern_vnpttech_libmanagement.dto.info;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
@Builder
@AllArgsConstructor
public class BookStatistics {

    private int month;

    private int year;

    private long totalBorrowedTurns;

    private Map<String, BookStatisticsDetails> details;

    public BookStatistics()
    {
        this.month=0;
        this.year=1970;
        this.totalBorrowedTurns =0;
        details = new HashMap<>();
    }

}
