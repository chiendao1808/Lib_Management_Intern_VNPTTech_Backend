package com.example.intern_vnpttech_libmanagement.controllers;

import com.example.intern_vnpttech_libmanagement.dto.request.BorrowRequest;
import com.example.intern_vnpttech_libmanagement.dto.response.MessageResponse;
import com.example.intern_vnpttech_libmanagement.entities.BorrowForm;
import com.example.intern_vnpttech_libmanagement.entities.LibStaff;
import com.example.intern_vnpttech_libmanagement.services.BookService;
import com.example.intern_vnpttech_libmanagement.services.BorrowFormDetailService;
import com.example.intern_vnpttech_libmanagement.services.BorrowFormService;
import com.example.intern_vnpttech_libmanagement.services.ReaderService;
import com.example.intern_vnpttech_libmanagement.security.SecurityService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;


@RestController
@RequestMapping(path = "libmng/api/borrow")
@Tag(name = "BorrowForm Controller")
public class BorrowFormController {

    @Autowired
    private ReaderService readerService;

    @Autowired
    private BookService bookService;

    @Autowired
    private BorrowFormService borrowFormService;

    @Autowired
    private BorrowFormDetailService borrowFormDetailService;

    @Autowired
    private SecurityService util;

    @Operation(summary = "Find borrow forms by reader")
    @GetMapping(path = "/find-by-reader")
    @SecurityRequirement(name = "methodAuth")
   public ResponseEntity<?> findByReader(@RequestParam(name = "reader_id")long readerId)
   {
       if(!readerService.findById(readerId).isPresent())
           return ResponseEntity.status(200).body(new MessageResponse("Reader not found","fail"));
       return ResponseEntity.status(200).body(borrowFormService.findByReader(readerId));
   }

   @Operation(summary = "Get all details of a borrow form")
   @GetMapping(path = "/details")
   @SecurityRequirement(name = "methodAuth")
   public ResponseEntity<?> getBorrowFormDetails(@RequestParam(name = "borrow_form_id") long borrowFormId,
                                                 @RequestParam(name = "page") int page,
                                                 Pageable pageable)
   {
       if(!borrowFormService.findByBorrowFormId(borrowFormId).isPresent())
           return ResponseEntity.status(200).body(new MessageResponse("Borrow Form not found","fail"));
//       PagedListHolder paged = new PagedListHolder(borrowFormDetailService.findByBorrowForm(borrowFormId).subList(page-1,page));
//       paged.setPage(page-1);
//       paged.setPageSize(1);
       return ResponseEntity.ok(borrowFormDetailService.findByBorrowForm(borrowFormId));
   }

   @Operation(summary = "Add a borrow form")
   @PostMapping (path = "/add")
   @SecurityRequirement(name = "methodAuth")
   public ResponseEntity<?> addBorrowForm(@RequestParam(name="reader_id")long readerId,
                                          @RequestBody @Validated BorrowRequest borrowRequest,
                                          HttpServletRequest request)
   {
       borrowRequest.setRequestType("POST");
       LibStaff libStaff = util.getStaffFromRequest(request);
       if(libStaff==null)
           throw new RuntimeException(""); // sử lý exception sau
       BorrowForm newBorrowForm = new BorrowForm();
       newBorrowForm.setDoStaff(libStaff);
       if(!readerService.findById(readerId).isPresent())
              return ResponseEntity.status(200).body(new MessageResponse("Reader not found","fail"));
       else newBorrowForm.setReader(readerService.findById(readerId).get());
       return borrowFormService.add(newBorrowForm, borrowRequest.getBorrowInfos()).isPresent()
                  ?ResponseEntity.status(201).body(new MessageResponse("Add borrow form successfully","success"))
                  :ResponseEntity.status(200).body(new MessageResponse("Add borrow form fail","fail"));
   }

   @Operation(summary = "Delete a borrow form")
    @DeleteMapping(path = "/delete")
   @SecurityRequirement(name = "methodAuth")
    public ResponseEntity<?> delete(@RequestParam(name = "borrow_form_id") long borrowFormId)
    {
        if(!borrowFormService.findByBorrowFormId(borrowFormId).isPresent())
            return ResponseEntity.status(200).body(new MessageResponse("Return form not found","fail"));
        return borrowFormService.delete(borrowFormId)
                ?ResponseEntity.status(200).body(new MessageResponse("Delete return form successfully","success"))
                :ResponseEntity.status(200).body(new MessageResponse("Delete return form fail","fail"));
    }
}
