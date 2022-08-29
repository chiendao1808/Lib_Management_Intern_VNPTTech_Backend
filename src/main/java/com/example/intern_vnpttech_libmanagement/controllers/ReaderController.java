package com.example.intern_vnpttech_libmanagement.controllers;

import com.example.intern_vnpttech_libmanagement.dto.response.MessageResponse;
import com.example.intern_vnpttech_libmanagement.dto.entity_dto.ReaderDTO;
import com.example.intern_vnpttech_libmanagement.entities.Reader;
import com.example.intern_vnpttech_libmanagement.services.ReaderService;
import io.swagger.v3.oas.annotations.OpenAPI30;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/libmng/api/reader")
@Tag(name = "Reader Controller ")
public class ReaderController {

    @Autowired
    private ReaderService readerService;

    @Operation(summary = "Find all readers")
    @GetMapping(path = "/find-all")
    @SecurityRequirement(name = "methodAuth")
    public ResponseEntity<?> findAll(Pageable pageable, @RequestParam(name = "page") int page)
    {
        pageable =PageRequest.of(page-1,1);
        return ResponseEntity.ok(readerService.findAll(pageable));
    }

    @Operation(summary = "Find a reader's by id")
    @GetMapping(path = "/find-by-id")
    @SecurityRequirement(name = "methodAuth")
    public ResponseEntity<?> findById(@RequestParam(name = "reader_id") long readerId)
    {
        return !readerService.findById(readerId).isPresent()
                ? ResponseEntity.status(200).body(new MessageResponse("Reader not found","fail"))
                : ResponseEntity.ok(readerService.findById(readerId));
    }

    @Operation(summary = "Find a reader by phone or email")
    @GetMapping("/find-by-phone-or-email")
    @SecurityRequirement(name = "methodAuth")
    public ResponseEntity<?> findByPhoneOrEmail(@RequestParam(name = "phone",required = false)String readerPhone,
                                                @RequestParam(name = "email",required = false) String readerEmail)
    {
        return readerEmail!=null || readerPhone!=null
                ?ResponseEntity.status(200).body(readerService.findByPhoneOrEmail(readerPhone,readerEmail))
                :ResponseEntity.status(200).body(new MessageResponse("Phone and Email are missing","fail"));
    }

    @Operation(summary = "Add a new reader")
    @PostMapping(path = "/add")
    @SecurityRequirement(name = "methodAuth")
    public ResponseEntity<?> add(@RequestBody Reader reader)
    {
        return readerService.add(reader).isPresent()
                ? ResponseEntity.status(201).body(new MessageResponse("Add reader successfully","success"))
                : ResponseEntity.status(200).body(new MessageResponse("Add reader fail","fail"));
    }

    @Operation(summary = "Update  reader's infos")
    @PutMapping(path = "/update")
    @SecurityRequirement(name = "methodAuth")
    public ResponseEntity<?> update(@RequestBody ReaderDTO readerDTO,
                                    @RequestParam(name = "reader_id") long readerId)
    {
        if(!readerService.findById(readerId).isPresent())
            return ResponseEntity.status(200).body(new MessageResponse("Reader not found","fail"));
        else readerDTO.setReaderId(readerId);
        return readerService.update(readerDTO).isPresent()
                ? ResponseEntity.status(201).body(new MessageResponse("Update reader successfully","success"))
                : ResponseEntity.status(200).body(new MessageResponse("Update reader fail","fail"));
    }

    @Operation(summary = "Delete a reader")
    @DeleteMapping(path = "/delete")
    @SecurityRequirement(name = "methodAuth")
    public ResponseEntity<?> delete(@RequestParam(name = "reader_id") long readerId)
    {
        return readerService.delete(readerId)
                ?ResponseEntity.status(200).body(new MessageResponse("Delete reader successfully","success"))
                :ResponseEntity.status(200).body(new MessageResponse("Delete reader fail","fail"));
    }


}
