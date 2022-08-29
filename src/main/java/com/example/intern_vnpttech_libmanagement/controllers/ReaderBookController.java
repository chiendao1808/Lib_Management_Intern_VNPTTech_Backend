package com.example.intern_vnpttech_libmanagement.controllers;

import com.example.intern_vnpttech_libmanagement.dto.response.MessageResponse;
import com.example.intern_vnpttech_libmanagement.services.BookService;
import com.example.intern_vnpttech_libmanagement.services.ReaderBookService;
import com.example.intern_vnpttech_libmanagement.services.ReaderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "libmng/api/reader-book")
@Tag(name = "ReaderBook Controller - Represent the relationship between Reader and Book (Borrow-Return relationship)")
public class ReaderBookController {

    @Autowired
    private ReaderService readerService;

    @Autowired
    private BookService bookService;

    @Autowired
    private ReaderBookService readerBookService;


    @Operation(summary = "Find readerbooks by reader")
    @GetMapping(path = "/find-by-reader")
    @SecurityRequirement(name = "methodAuth")
    public ResponseEntity<?> findByReader(@RequestParam(name = "reader_id")long readerId)
    {
        if(!readerService.findById(readerId).isPresent())
            return ResponseEntity.status(200).body(new MessageResponse("Reader not found","fail"));
        return ResponseEntity.status(200).body(readerBookService.findByReader(readerId));
    }
}
