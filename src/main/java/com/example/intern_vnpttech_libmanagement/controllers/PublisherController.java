package com.example.intern_vnpttech_libmanagement.controllers;

import com.example.intern_vnpttech_libmanagement.dto.entity_dto.PublisherDTO;
import com.example.intern_vnpttech_libmanagement.dto.response.MessageResponse;
import com.example.intern_vnpttech_libmanagement.entities.Publisher;
import com.example.intern_vnpttech_libmanagement.services.PublisherService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping(path = "libmng/api/publisher")
@Tag(name = "Publisher Controller")
public class PublisherController {

    @Autowired
    private PublisherService publisherService;

    @Operation(summary = "Find a publisher by id")
    @GetMapping(path = "/find-by-id")
    @SecurityRequirement(name = "methodAuth")
    public ResponseEntity<?> findById(@RequestParam(name = "publisher_id")long publisherId,
                                      HttpServletRequest request)
    {
        return publisherService.findById(publisherId).isPresent()
                ?ResponseEntity.ok(publisherService.findById(publisherId))
                :ResponseEntity.status(200).body(new MessageResponse("Publisher not found","fail"));
    }

    @Operation(summary = "Find a publisher by phone or email")
    @GetMapping(path = "/find-by-phone-or-email")
    @SecurityRequirement(name ="methodAuth")
    public ResponseEntity<?> findByPhoneOrEmail(@RequestParam(name = "phone") String phone,
                                                @RequestParam(name = "email") String email,
                                                HttpServletRequest request)
    {
        return !publisherService.findByPhoneOrEmail(phone,email).isPresent()
                ?ResponseEntity.ok(publisherService.findByPhoneOrEmail(phone,email))
                :ResponseEntity.status(200).body(new MessageResponse("Publisher not found (by email or phone)","fail"));
    }


    @Operation(summary = "Add a new publisher")
    @PostMapping(path = "/add")
    @SecurityRequirement(name ="methodAuth")
    public ResponseEntity<?> add(@RequestBody Publisher publisher)
    {
      return publisherService.add(publisher).isPresent()
              ?ResponseEntity.status(201).body(new MessageResponse("Add  new publisher successfully","success"))
              :ResponseEntity.status(200).body(new MessageResponse("Add new publisher fail","fail"));
    }

    @Operation(summary = "Update a publisher's infos")
    @PutMapping(path = "/update")
    public ResponseEntity<?> update(@RequestBody PublisherDTO publisherDTO,
                                    @RequestParam(name = "publisher_id") long publisherId,
                                    HttpServletRequest request)
    {
        return publisherService.findById(publisherId).map(publisher -> {
            publisherDTO.setPublisherId(publisherId);
            return publisherService.update(publisherDTO).isPresent()
                    ?ResponseEntity.status(200).body(new MessageResponse("Update publisher successfully","success"))
                    :ResponseEntity.status(200).body(new MessageResponse("Update publisher fail","fail"));

        }).orElse(ResponseEntity.status(200).body(new MessageResponse("Publisher not found","fail")));
    }

    @Operation(summary = "Delete a publisher")
    @PutMapping(path = "/delete")
    @SecurityRequirement(name = "methodAuth")
    public ResponseEntity<?> delete(@RequestParam(name = "publisher_id")long publisherId)
    {
        return publisherService.delete(publisherId)
                ?ResponseEntity.status(200).body(new MessageResponse("Delete the publisher successfully","success"))
                :ResponseEntity.status(200).body(new MessageResponse("Delete the publisher fail","fail"));
    }
}
