package com.example.intern_vnpttech_libmanagement.serviceimpls;

import com.example.intern_vnpttech_libmanagement.dto.entity_dto.LibStaffDTO;
import com.example.intern_vnpttech_libmanagement.entities.Staff;
import com.example.intern_vnpttech_libmanagement.repositories.StaffRepo;
import com.example.intern_vnpttech_libmanagement.services.StaffService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class StaffServiceImpl implements StaffService {

    @Autowired
    private StaffRepo staffRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public List<Staff> findAll() {
        try{
            return staffRepo.findAll();
        } catch (Exception ex)
        {
            log.error("Find all staff error",ex);
            return Collections.emptyList();
        }
    }

    @Override
    public Optional<Staff> findById(long staffId) {
        try{
            return staffRepo.findById(staffId);
        } catch (Exception ex)
        {
            log.error("Find staff by id error",ex);
            return Optional.empty();
        }
    }

    @Override
    public Optional<Staff> findByUsernameOrEmailOrPhone(String staffUsername, String staffEmail) {
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
    public Optional<Staff> add(Staff staff) {
        try{
            if(staffRepo.existsByStaffUsernameOrStaffEmailOrStaffPhone( staff.getStaffUsername(),
                                                                        staff.getStaffEmail(),
                                                                        staff.getStaffPhone())>0)
            throw new RuntimeException("Staff is existed");
            staff.setStaffPassword(passwordEncoder.encode(staff.getStaffPassword()));
            String roleFormatted = "ROLE_"+ staff.getStaffRole().toUpperCase();
            staff.setStaffRole(roleFormatted);
            return Optional.ofNullable(staffRepo.save(staff));
        } catch (Exception ex)
        {
            log.error("Add a staff error",ex);
            return Optional.empty();
        }
    }

    @Override
    public Optional<Staff> update(LibStaffDTO staffDTO) {
        try{
            Staff staffToUpdate = staffRepo.findById(staffDTO.getStaffId()).get();
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
