package com.example.intern_vnpttech_libmanagement.dto.entity_dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BookRecordDTO {

    private  long bookId;

    private String bookCode;

    private String bookType;

    private String bookAuthor;

    private String bookName;

    private String bookState;

    private String bookImageURL;

    private Boolean available;

    private Integer bookPublishYear;
}
