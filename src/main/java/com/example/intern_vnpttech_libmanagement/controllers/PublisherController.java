package com.example.intern_vnpttech_libmanagement.controllers;

import com.example.intern_vnpttech_libmanagement.dto.entity_dto.PublisherDTO;
import com.example.intern_vnpttech_libmanagement.dto.response.MessageResponse;
import com.example.intern_vnpttech_libmanagement.entities.Publisher;
import com.example.intern_vnpttech_libmanagement.services.PublisherService;
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
@RequestMapping(path = "libmng/api/publisher")
@Tag(name = "Publisher Controller")
public class PublisherController {

    @Autowired
    private PublisherService publisherService;


    @Operation(summary = "Filter publishers")
    @GetMapping
    @SecurityRequirement(name = "methodAuth")
    public ResponseEntity<?> get(@RequestParam(name = "publisher_id", required = false,defaultValue = "-1") Long publisherId,
                                 @RequestParam(name = "publisher_name",required = false,defaultValue = "ALL") String publisherName,
                                 @RequestParam(name = "publisher_phone",required = false,defaultValue = "ALL") String publisherPhone,
                                 @RequestParam(name = "publisher_email",required = false,defaultValue = "ALL") String publisherEmail,
                                 @RequestParam(name = "publisher_fax",required = false,defaultValue = "ALL") String publisherFax,
                                 @RequestParam(name = "page",required = false,defaultValue = "1") int page,
                                 @RequestParam(name = "size",required = false,defaultValue = "1") int size,
                                 Pageable pageable,
                                 HttpServletRequest request)
    {
        pageable = PageRequest.of(page-1,size);
        return ResponseEntity.ok(publisherService.findByCriteria(publisherId,publisherName, publisherPhone,publisherEmail,publisherFax,pageable));
    }


    @Operation(summary = "Add a new publisher")
    @PostMapping()
    @SecurityRequirement(name ="methodAuth")
    public ResponseEntity<?> add(@RequestBody Publisher publisher)
    {
      return publisherService.add(publisher).isPresent()
              ?ResponseEntity.status(201).body(new MessageResponse("Add  new publisher successfully","success"))
              :ResponseEntity.status(200).body(new MessageResponse("Add new publisher fail","fail"));
    }

    @Operation(summary = "Update a publisher's infos")
    @PutMapping()
    public ResponseEntity<?> update(@RequestBody PublisherDTO publisherDTO,
                                    HttpServletRequest request)
    {
        return publisherService.findById(publisherDTO.getPublisherId()).map(publisher -> {
            return publisherService.update(publisherDTO).isPresent()
                    ?ResponseEntity.status(200).body(new MessageResponse("Update publisher successfully","success"))
                    :ResponseEntity.status(200).body(new MessageResponse("Update publisher fail","fail"));

        }).orElse(ResponseEntity.status(200).body(new MessageResponse("Publisher not found","fail")));
    }

    @Operation(summary = "Delete a publisher")
    @DeleteMapping (path = "/{publisher_id}")
    @SecurityRequirement(name = "methodAuth")
    public ResponseEntity<?> delete(@PathVariable(name = "publisher_id")long publisherId)
    {
        return publisherService.delete(publisherId)
                ?ResponseEntity.status(200).body(new MessageResponse("Delete the publisher successfully","success"))
                :ResponseEntity.status(200).body(new MessageResponse("Delete the publisher fail","fail"));
    }
}
