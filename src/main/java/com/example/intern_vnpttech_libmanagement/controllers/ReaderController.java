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
    @GetMapping(path = "/get")
    @SecurityRequirement(name = "methodAuth")
    public ResponseEntity<?> get(@RequestParam(name = "reader_id",required = false,defaultValue = "-1") Long readerId,
                                 @RequestParam(name = "reader_name",required = false,defaultValue = "--null--")String readerName,
                                 @RequestParam(name = "reader_phone",required = false,defaultValue = "0000000000")String readerPhone,
                                 @RequestParam(name = "reader_email",required = false,defaultValue = "abc@gmail.com") String readerEmail,
                                 @RequestParam(name = "allreaders",required = false,defaultValue = "false") Boolean all,
                                 @RequestParam(name = "page",defaultValue = "1") int page,
                                 Pageable pageable,
                                 HttpServletRequest request)
    {
        pageable =PageRequest.of(page-1,2);
        if(all.booleanValue()==true)
        {
            return ResponseEntity.ok(readerService.findAll(pageable));
        }
        return ResponseEntity.ok(readerService.findByCriteria(readerId,readerName,readerPhone,readerEmail,pageable));
    }

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
    @DeleteMapping(path = "/delete/{reader_id}")
    @SecurityRequirement(name = "methodAuth")
    public ResponseEntity<?> delete(@PathVariable(name = "reader_id") Long readerId)
    {
        return readerService.delete(readerId)
                ?ResponseEntity.status(200).body(new MessageResponse("Delete reader successfully","success"))
                :ResponseEntity.status(200).body(new MessageResponse("Delete reader fail","fail"));
    }


}
