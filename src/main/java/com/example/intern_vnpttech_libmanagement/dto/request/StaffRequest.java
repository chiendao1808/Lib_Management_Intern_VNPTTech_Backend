package com.example.intern_vnpttech_libmanagement.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StaffRequest {

    private String staffUsername;

    private String staffEmail;
}
