package com.example.intern_vnpttech_libmanagement.services;

import com.example.intern_vnpttech_libmanagement.dto.entity_dto.LibStaffDTO;
import com.example.intern_vnpttech_libmanagement.entities.LibStaff;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface LibStaffService {

    List<LibStaff> findAll();

    Optional<LibStaff> findById(long staffId);

    Optional<LibStaff> findByUsernameOrEmailOrPhone(String staffUsername,String staffEmail);

    Optional<LibStaff> add(LibStaff libStaff);

    Optional<LibStaff> update(LibStaffDTO staffDTO);

    boolean delete(long staffId);
}
