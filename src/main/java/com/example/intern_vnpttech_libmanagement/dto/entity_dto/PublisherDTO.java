package com.example.intern_vnpttech_libmanagement.dto.entity_dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PublisherDTO {

    private long publisherId;

    private String publisherName;

    private String publisherPhone;

    private String publisherEmail;

    private String publisherFax;

    private String publisherAddress;

    private Boolean deleted;
}
