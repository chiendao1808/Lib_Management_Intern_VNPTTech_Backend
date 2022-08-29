package com.example.intern_vnpttech_libmanagement.dto.entity_dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReaderCardDTO {

    private long cardId;

    private Float minusAmount;

    private Integer extendMonths;
}
