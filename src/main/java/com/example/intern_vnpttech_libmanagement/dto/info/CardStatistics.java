package com.example.intern_vnpttech_libmanagement.dto.info;

import com.example.intern_vnpttech_libmanagement.entities.ReaderCardType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@Data
@Builder
@AllArgsConstructor
public class CardStatistics {

     private int month;

     private int year;

     private long totalNumberPublishedCardInMonth;

     private Map<String,CardStatisticsDetails> detailNumbers;

     public CardStatistics()
     {
          this.month=0;
          this.year=1970;
          this.totalNumberPublishedCardInMonth=0;
          detailNumbers = new HashMap<>();
     }

}
