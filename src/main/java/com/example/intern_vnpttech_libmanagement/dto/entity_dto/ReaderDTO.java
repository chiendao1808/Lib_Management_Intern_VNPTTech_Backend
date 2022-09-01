package com.example.intern_vnpttech_libmanagement.dto.entity_dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReaderDTO {

    private  Long readerId;

    private String readerName;

    private String readerEmail;

    private String readerPhone;

    private String readerAddress;
}
