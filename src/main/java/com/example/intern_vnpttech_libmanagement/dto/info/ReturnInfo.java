package com.example.intern_vnpttech_libmanagement.dto.info;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.autoconfigure.batch.BatchDataSource;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReturnInfo {

    private long returnBookId;

    private String returnedBookState;

    private Float paidAmount;
}
