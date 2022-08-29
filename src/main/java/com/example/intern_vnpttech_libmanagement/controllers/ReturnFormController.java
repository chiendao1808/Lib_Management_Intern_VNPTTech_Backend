package com.example.intern_vnpttech_libmanagement.controllers;

import com.example.intern_vnpttech_libmanagement.dto.request.ReturnRequest;
import com.example.intern_vnpttech_libmanagement.dto.response.MessageResponse;
import com.example.intern_vnpttech_libmanagement.entities.Staff;
import com.example.intern_vnpttech_libmanagement.entities.ReturnForm;
import com.example.intern_vnpttech_libmanagement.services.ReaderService;
import com.example.intern_vnpttech_libmanagement.services.ReturnFormDetailService;
import com.example.intern_vnpttech_libmanagement.services.ReturnFormService;
import com.example.intern_vnpttech_libmanagement.security.SecurityService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping(path = "libmng/api/return")
@Tag(name = "ReturnForm Controller")
public class ReturnFormController {

    @Autowired
    private ReturnFormService returnFormService;

    @Autowired
    private ReaderService readerService;

    @Autowired
    private ReturnFormDetailService returnFormDetailService;

    @Autowired
    private SecurityService util;

    @Operation(summary = "Find return forms by reader")
    @GetMapping(path = "/find-by-reader")
    @SecurityRequirement(name = "methodAuth")
    public ResponseEntity<?> findByReader(@RequestParam(name = "reader_id")long readerId)
    {
        if(!readerService.findById(readerId).isPresent())
            return ResponseEntity.status(204).body(new MessageResponse("Reader not found","fail"));
        return ResponseEntity.ok(returnFormService.findByReader(readerId));
    }

    @Operation(summary = "Get all details of a return form")
    @GetMapping(path = "/details")
    @SecurityRequirement(name = "methodAuth")
    public ResponseEntity<?> getReturnFormDetails(@RequestParam(name = "return_form_id")long returnFormId)
    {
        if(!returnFormService.findByReturnFormId(returnFormId).isPresent())
            return ResponseEntity.status(200).body(new MessageResponse("Return form not found","fail"));
        return ResponseEntity.status(200).body(returnFormDetailService.findByReturnForm(returnFormId));
    }

    @PostMapping(path = "/add")
    @Operation(summary = "Add a return form")
    @SecurityRequirement(name = "methodAuth")
    public ResponseEntity<?> add(@RequestParam(name = "reader_id") long readerId,
                                 @RequestBody ReturnRequest returnRequest,
                                 HttpServletRequest request)
    {
        returnRequest.setRequestType("POST");
        Staff staff = util.getStaffFromRequest(request);
        if(staff ==null)
            return ResponseEntity.status(200).body(new MessageResponse("Can't not get staff from request","fail"));
        ReturnForm returnForm = new ReturnForm();
        returnForm.setDidStaff(staff);
        if(!readerService.findById(readerId).isPresent())
            return ResponseEntity.status(200).body(new MessageResponse("Reader not found","fail"));
        else returnForm.setReader(readerService.findById(readerId).get());
        return returnFormService.add(returnForm,returnRequest.getReturnInfos()).isPresent()
                ?ResponseEntity.status(201).body(new MessageResponse("Add return form successfully","success"))
                :ResponseEntity.status(200).body(new MessageResponse("Add return form fail","fail"));
    }

    @Operation(summary = "Delete a return form")
    @DeleteMapping(path = "/delete")
    @SecurityRequirement(name = "methodAuth")
    public ResponseEntity<?> delete(@RequestParam(name = "return_form_id") long returnFormId)
    {
        if(!returnFormService.findByReturnFormId(returnFormId).isPresent())
            return ResponseEntity.status(200).body(new MessageResponse("Return form not found","fail"));
        return returnFormService.delete(returnFormId)
                ?ResponseEntity.status(200).body(new MessageResponse("Delete return form successfully","success"))
                :ResponseEntity.status(200).body(new MessageResponse("Delete return form fail","fail"));
    }

}
