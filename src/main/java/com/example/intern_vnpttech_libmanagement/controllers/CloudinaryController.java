package com.example.intern_vnpttech_libmanagement.controllers;

import com.example.intern_vnpttech_libmanagement.dto.entity_dto.BookRecordDTO;
import com.example.intern_vnpttech_libmanagement.dto.response.MessageResponse;
import com.example.intern_vnpttech_libmanagement.serviceimpls.upload_file.CloudinaryService;
import com.example.intern_vnpttech_libmanagement.services.BookService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping(path = "libmng/api/")
@Tag(name = "Cloudinary Controller - Upload Image")
public class CloudinaryController {

    @Autowired
    private  CloudinaryService cloudinaryService;

    @Autowired
    private BookService bookService;


    @Operation(summary = "Upload a book's image")
    @PostMapping(path ="/book/upload")
    @SecurityRequirement(name = "methodAuth")
    public ResponseEntity<?> uploadBookImage(@RequestParam(name = "book_code") String bookCode,
                                             @RequestParam(name = "image")MultipartFile file)
    {
        if(bookService.findByBookCode(bookCode).isEmpty())
            return ResponseEntity.status(200).body(new MessageResponse("Book not found","fail"));
        String imageURL = cloudinaryService.upload(file,"image");
        if(imageURL == null || imageURL.equals(""))
            return ResponseEntity.status(200).body(new MessageResponse("Upload book's image fail","fail"));
        return bookService.updateAll(BookRecordDTO.builder().bookCode(bookCode).bookImageURL(imageURL).build()).isPresent()
                ?ResponseEntity.status(201).body(new MessageResponse("Upload the book's image successfully. URL:"+imageURL,"success"))
                :ResponseEntity.status(200).body(new MessageResponse("Upload the book's image fail","fail"));
    }


}
