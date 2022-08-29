package com.example.intern_vnpttech_libmanagement.services;

import com.example.intern_vnpttech_libmanagement.dto.entity_dto.LibStaffDTO;
import com.example.intern_vnpttech_libmanagement.entities.Staff;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface StaffService {

    List<Staff> findAll();

    Optional<Staff> findById(long staffId);

    Optional<Staff> findByUsernameOrEmailOrPhone(String staffUsername, String staffEmail);

    Optional<Staff> add(Staff staff);

    Optional<Staff> update(LibStaffDTO staffDTO);

    boolean delete(long staffId);
}
