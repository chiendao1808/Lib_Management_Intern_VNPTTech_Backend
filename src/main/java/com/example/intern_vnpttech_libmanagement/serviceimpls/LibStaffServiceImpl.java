package com.example.intern_vnpttech_libmanagement.serviceimpls;

import com.example.intern_vnpttech_libmanagement.dto.entity_dto.LibStaffDTO;
import com.example.intern_vnpttech_libmanagement.entities.LibStaff;
import com.example.intern_vnpttech_libmanagement.repositories.LibStaffRepo;
import com.example.intern_vnpttech_libmanagement.services.LibStaffService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class LibStaffServiceImpl implements LibStaffService {

    @Autowired
    private LibStaffRepo staffRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public List<LibStaff> findAll() {
        try{
            return staffRepo.findAll();
        } catch (Exception ex)
        {
            log.error("Find all staff error",ex);
            return Collections.emptyList();
        }
    }

    @Override
    public Optional<LibStaff> findById(long staffId) {
        try{
            return staffRepo.findById(staffId);
        } catch (Exception ex)
        {
            log.error("Find staff by id error",ex);
            return Optional.empty();
        }
    }

    @Override
    public Optional<LibStaff> findByUsernameOrEmailOrPhone(String staffUsername, String staffEmail) {
        try{
            return staffRepo.findByUsernameOrEmail(staffUsername!=null?staffUsername:"",
                                                    staffEmail!=null?staffEmail:"");
        } catch (Exception ex)
        {
            log.error("Find staff by username or email error",ex);
            return Optional.empty();
        }
    }

    @Override
    public Optional<LibStaff> add(LibStaff libStaff) {
        try{
            if(staffRepo.existsByStaffUsernameOrStaffEmailOrStaffPhone( libStaff.getStaffUsername(),
                                                                        libStaff.getStaffEmail(),
                                                                        libStaff.getStaffPhone())>0)
            throw new RuntimeException("Staff is existed");
            libStaff.setStaffPassword(passwordEncoder.encode(libStaff.getStaffPassword()));
            String roleFormatted = "ROLE_"+libStaff.getStaffRole().toUpperCase();
            libStaff.setStaffRole(roleFormatted);
            return Optional.ofNullable(staffRepo.save(libStaff));
        } catch (Exception ex)
        {
            log.error("Add a staff error",ex);
            return Optional.empty();
        }
    }

    @Override
    public Optional<LibStaff> update(LibStaffDTO staffDTO) {
        try{
            LibStaff staffToUpdate = staffRepo.findById(staffDTO.getStaffId()).get();
            staffToUpdate.setStaffPhone(staffDTO.getStaffPhone()!=null?staffDTO.getStaffPhone():staffToUpdate.getStaffPhone());
            staffToUpdate.setStaffAddress(staffDTO.getStaffEmail()!=null?staffDTO.getStaffAdress(): staffToUpdate.getStaffAddress());
            staffToUpdate.setStaffRole(staffDTO.getStaffRole()!=null?staffDTO.getStaffRole(): staffToUpdate.getStaffRole());
            return Optional.ofNullable(staffRepo.save(staffToUpdate));
        } catch (Exception ex)
        {
            log.error("Update staff error",ex);
            return Optional.empty();
        }
    }

    @Override
    public boolean delete(long staffId) {
        try{
            return staffRepo.delete(staffId)>0?true:false;
        } catch (Exception ex)
        {
            log.error("Delete a staff error");
            return false;
        }
    }
}
