package com.example.intern_vnpttech_libmanagement.dto.info;

import com.example.intern_vnpttech_libmanagement.entities.ReaderCardType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CardStatistics {

     private int month;

     private int year;

     private long numberPublishedCardInMonth;

     private Map<String,CardStatisticsDetails> detailNumbers;

}
