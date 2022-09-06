package com.example.intern_vnpttech_libmanagement.controllers;

import com.example.intern_vnpttech_libmanagement.dto.response.MessageResponse;
import com.example.intern_vnpttech_libmanagement.dto.entity_dto.ReaderDTO;
import com.example.intern_vnpttech_libmanagement.entities.Reader;
import com.example.intern_vnpttech_libmanagement.services.ReaderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping(path = "/libmng/api/reader")
@Tag(name = "Reader Controller ")
public class ReaderController {

    @Autowired
    private ReaderService readerService;


    /*
    * Filter API for reader
    */
    @Operation(summary = "Get readers with a criteria")
    @GetMapping()
    @SecurityRequirement(name = "methodAuth")
    public ResponseEntity<?> get(@RequestParam(name = "reader_id",required = false,defaultValue = "-1") Long readerId,
                                 @RequestParam(name = "reader_name",required = false,defaultValue = "ALL")String readerName,
                                 @RequestParam(name = "reader_phone",required = false,defaultValue = "ALL")String readerPhone,
                                 @RequestParam(name = "reader_email",required = false,defaultValue = "ALL") String readerEmail,
                                 @RequestParam(name = "page",defaultValue = "1") int page,
                                 @RequestParam(name = "size",defaultValue = "1") int size,
                                 Pageable pageable,
                                 HttpServletRequest request)
    {
        pageable =PageRequest.of(page-1,size);
        return ResponseEntity.ok(readerService.findByCriteria(readerId,readerName,readerPhone,readerEmail,pageable));
    }

    @Operation(summary = "Add a new reader")
    @PostMapping()
    @SecurityRequirement(name = "methodAuth")
    public ResponseEntity<?> add(@RequestBody Reader reader)
    {
        return readerService.add(reader).isPresent()
                ? ResponseEntity.status(201).body(new MessageResponse("Add reader successfully","success"))
                : ResponseEntity.status(200).body(new MessageResponse("Add reader fail","fail"));
    }

    @Operation(summary = "Update  reader's infos")
    @PutMapping()
    @SecurityRequirement(name = "methodAuth")
    public ResponseEntity<?> update(@RequestBody ReaderDTO readerDTO)
    {
        if(!readerService.findById(readerDTO.getReaderId()).isPresent())
            return ResponseEntity.status(200).body(new MessageResponse("Reader not found","fail"));
        else readerDTO.setReaderId(readerDTO.getReaderId());
        return readerService.update(readerDTO).isPresent()
                ? ResponseEntity.status(201).body(new MessageResponse("Update reader successfully","success"))
                : ResponseEntity.status(200).body(new MessageResponse("Update reader fail","fail"));
    }

    @Operation(summary = "Delete a reader")
    @DeleteMapping(path = "/{reader_id}")
    @SecurityRequirement(name = "methodAuth")
    public ResponseEntity<?> delete(@PathVariable(name = "reader_id") Long readerId)
    {
        return readerService.delete(readerId)
                ?ResponseEntity.status(200).body(new MessageResponse("Delete reader successfully","success"))
                :ResponseEntity.status(200).body(new MessageResponse("Delete reader fail","fail"));
    }


}
