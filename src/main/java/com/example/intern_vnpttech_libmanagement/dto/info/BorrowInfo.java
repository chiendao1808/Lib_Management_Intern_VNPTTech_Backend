package com.example.intern_vnpttech_libmanagement.dto.info;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BorrowInfo {

    private int bookId;

    private int borrowDays;
}
