package com.example.intern_vnpttech_libmanagement.controllers;

import com.example.intern_vnpttech_libmanagement.dto.entity_dto.LibStaffDTO;
import com.example.intern_vnpttech_libmanagement.dto.request.ChangePasswordRequest;
import com.example.intern_vnpttech_libmanagement.dto.request.StaffRequest;
import com.example.intern_vnpttech_libmanagement.dto.response.MessageResponse;
import com.example.intern_vnpttech_libmanagement.entities.Staff;
import com.example.intern_vnpttech_libmanagement.security.SecurityService;
import com.example.intern_vnpttech_libmanagement.services.StaffService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

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

    @Operation(summary = "Find a staff by id")
    @GetMapping(path = "/find-by-id")
    @SecurityRequirement(name = "methodAuth")
    @RolesAllowed("ROLE_MANAGER")
    public ResponseEntity<?> findById(@RequestParam(name = "staff_id") long staffId)
    {
        return staffService.findById(staffId).isPresent()
                ?ResponseEntity.status(200).body(staffService.findById(staffId))
                :ResponseEntity.status(200).body(new MessageResponse("Staff not found","fail"));
    }

    @Operation(summary = "Find a staff by username or email")
    @GetMapping(path = "/find-by-username-or-email")
    @SecurityRequirement(name = "methodAuth")
    @RolesAllowed("ROLE_MANAGER")
    public ResponseEntity<?> findByUsernameOrEmail(@RequestBody StaffRequest staffRequest)
    {
        Optional<Staff> staffOptional = staffService.findByUsernameOrEmailOrPhone(staffRequest.getStaffUsername(),staffRequest.getStaffEmail());
        return staffOptional.isPresent()?ResponseEntity.ok(staffOptional)
                :ResponseEntity.status(200).body(new MessageResponse("Staff not found","fail"));
    }

    // đăng ký mới chuyển qua Auth controller

    @Operation(summary = "Update staff's infos (self-update)")
    @PutMapping(path = "/update")
    @SecurityRequirement(name = "methodAuth")
    public ResponseEntity<?> update(@RequestBody LibStaffDTO staffDTO,
                                    HttpServletRequest request)
    {
        long staffId = securityService.getStaffFromRequest(request).getStaffId();
        staffDTO.setStaffId(staffId);
//        if(!libStaffService.findById(staffId).isPresent())
//            return ResponseEntity.status(204).body(new MessageResponse("Staff not found","fail"));
//        else staffDTO.setStaffId(staffId);
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
//        if(!libStaffService.findById(staffId).isPresent())
//            return ResponseEntity.status(204).body(new MessageResponse("Staff not found","fail"));
        String encodedCurrentPass = staffService.findById(staffId).get().getStaffPassword();
        if(!passwordEncoder.matches(changePasswordRequest.getCurrentPassword(),encodedCurrentPass))
            return ResponseEntity.status(200).body(new MessageResponse("Current password was wrong","fail"));
        return staffService.update(LibStaffDTO.builder()
                                                    .staffId(staffId)
                                                    .staffPassword(changePasswordRequest.getNewPassword())
                                                    .build()).isPresent()
                ? ResponseEntity.status(201).body(new MessageResponse("Change password successfully","Success"))
                : ResponseEntity.status(200).body((new MessageResponse("Change password fail","fail")));

    }

    @Operation(summary = "Delete a staff")
    @DeleteMapping(path = "/delete")
    @SecurityRequirement(name = "methodAuth")
    @RolesAllowed("ROLE_MANAGER")
    public ResponseEntity<?> delete(@RequestParam(name = "staff_id")long staffId)
    {
        return staffService.delete(staffId)
                ?ResponseEntity.status(200).body(new MessageResponse("Delete staff successfully","success"))
                :ResponseEntity.status(200).body(new MessageResponse("Delete staff fail","fail"));
    }


}
