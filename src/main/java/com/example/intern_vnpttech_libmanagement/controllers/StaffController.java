package com.example.intern_vnpttech_libmanagement.controllers;

import com.example.intern_vnpttech_libmanagement.dto.entity_dto.StaffDTO;
import com.example.intern_vnpttech_libmanagement.dto.request.ChangePasswordRequest;
import com.example.intern_vnpttech_libmanagement.dto.response.MessageResponse;
import com.example.intern_vnpttech_libmanagement.security.SecurityService;
import com.example.intern_vnpttech_libmanagement.services.StaffService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping(path = "libmng/api/staff")
@Tag(name = "Staff Controller")
public class StaffController {

    @Autowired
    private StaffService staffService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private SecurityService securityService;

    @Operation(summary = "Filter staffs")
    @GetMapping
    @SecurityRequirement(name = "methodAuth")
    @RolesAllowed({"ROLE_MANAGER"})
    public ResponseEntity<?> get(@RequestParam(name = "staff_id",required = false,defaultValue = "-1")Long staffId,
                                 @RequestParam(name = "staff_username",required = false,defaultValue = "ALL") String staffUsername,
                                 @RequestParam(name = "staff_email",required = false,defaultValue = "ALL")String staffEmail,
                                 @RequestParam(name = "staff_name",required = false,defaultValue = "ALL") String staffName,
                                 @RequestParam(name = "staff_phone",required = false,defaultValue = "ALL")String staffPhone,
                                 @RequestParam(name = "page",required = false,defaultValue = "1") int page,
                                 @RequestParam(name = "size",required = false,defaultValue = "1") int size,
                                 Pageable pageable,
                                 HttpServletRequest request)
    {
        pageable = PageRequest.of(page-1, size);
        return ResponseEntity.ok(staffService.findByCreterias(staffId,staffUsername,staffEmail,staffName,staffPhone,pageable));
    }

    // đăng ký mới chuyển qua Auth controller

    @Operation(summary = "Update staff's infos (self-update)")
    @PutMapping
    @SecurityRequirement(name = "methodAuth")
    public ResponseEntity<?> update(@RequestBody StaffDTO staffDTO,
                                    HttpServletRequest request)
    {
        long staffId = securityService.getStaffFromRequest(request).getStaffId();
        staffDTO.setStaffId(staffId);
        return staffService.update(staffDTO).isPresent()
                ? ResponseEntity.status(201).body(new MessageResponse("Update staff successfully","success"))
                : ResponseEntity.status(200).body(new MessageResponse("Update staff fail","fail"));
    }

    @Operation(summary = "Change staff's password (self-change)")
    @PutMapping(path = "/change-password")
    @SecurityRequirement(name = "methodAuth")
    public ResponseEntity<?> changePassword(@RequestBody ChangePasswordRequest changePasswordRequest,
                                            HttpServletRequest request)
    {
        long staffId = securityService.getStaffFromRequest(request).getStaffId();
        String encodedCurrentPass = staffService.findById(staffId).get().getStaffPassword();
        if(!passwordEncoder.matches(changePasswordRequest.getCurrentPassword(),encodedCurrentPass))
            return ResponseEntity.status(200).body(new MessageResponse("Current password was wrong","fail"));
        return staffService.update(StaffDTO.builder()
                                                    .staffId(staffId)
                                                    .staffPassword(changePasswordRequest.getNewPassword())
                                                    .build()).isPresent()
                ? ResponseEntity.status(201).body(new MessageResponse("Change password successfully","Success"))
                : ResponseEntity.status(200).body((new MessageResponse("Change password fail","fail")));

    }

    @Operation(summary = "Delete a staff")
    @DeleteMapping(path = "/{staff_id}")
    @SecurityRequirement(name = "methodAuth")
    @RolesAllowed("ROLE_MANAGER")
    public ResponseEntity<?> delete(@PathVariable(name = "staff_id")long staffId)
    {
        return staffService.delete(staffId)
                ?ResponseEntity.status(200).body(new MessageResponse("Delete staff successfully","success"))
                :ResponseEntity.status(200).body(new MessageResponse("Delete staff fail","fail"));
    }

}
