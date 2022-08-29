package com.example.intern_vnpttech_libmanagement.dto.info;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BookStatisticsDetails {

    private long numberBorrowedTurns;

    private float ratio;
}
