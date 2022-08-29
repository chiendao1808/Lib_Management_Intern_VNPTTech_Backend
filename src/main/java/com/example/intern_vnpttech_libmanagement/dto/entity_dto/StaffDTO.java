package com.example.intern_vnpttech_libmanagement.dto.entity_dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StaffDTO {

    private Long staffId;

    private String staffPassword;

    private String staffName;

    private String staffEmail;

    private String staffPhone;

    private String staffAdress;

    private String staffRole;
}
