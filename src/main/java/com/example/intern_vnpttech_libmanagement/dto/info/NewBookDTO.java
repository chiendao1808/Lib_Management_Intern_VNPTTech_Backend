package com.example.intern_vnpttech_libmanagement.dto.info;

import com.example.intern_vnpttech_libmanagement.entities.Book;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NewBookDTO {

    @NotNull
    private Book book;

    @NotNull
    private Long publisherId;

    @NotNull
    private Long bookTypeId;

    @NotNull
    private Integer amount;
}
