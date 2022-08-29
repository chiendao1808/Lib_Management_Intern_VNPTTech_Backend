package com.example.intern_vnpttech_libmanagement.dto.request;

import com.example.intern_vnpttech_libmanagement.dto.info.ReturnInfo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReturnRequest {

    private String requestType;

    private ReturnInfo[] returnInfos;
}
