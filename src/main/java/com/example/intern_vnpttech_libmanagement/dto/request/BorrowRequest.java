package com.example.intern_vnpttech_libmanagement.dto.request;

import com.example.intern_vnpttech_libmanagement.dto.info.BorrowInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BorrowRequest {

    private String requestType ;

    private BorrowInfo[] borrowInfos;
}
