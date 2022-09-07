package com.example.intern_vnpttech_libmanagement.services;

import com.example.intern_vnpttech_libmanagement.dto.entity_dto.StaffDTO;
import com.example.intern_vnpttech_libmanagement.entities.Staff;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface StaffService {

    Page<Staff> findByCreterias(Long staffId, String staffUsername, String staffEmail, String staffName, String staffPhone, Pageable pageable);

    List<Staff> findAll();

    Optional<Staff> findById(long staffId);

    Optional<Staff> findByUsernameOrEmailOrPhone(String staffUsername, String staffEmail);

    Optional<Staff> add(Staff staff);

    Optional<Staff> update(StaffDTO staffDTO);

    boolean delete(long staffId);
}
