package com.example.intern_vnpttech_libmanagement.security;

import com.example.intern_vnpttech_libmanagement.entities.Staff;
import com.example.intern_vnpttech_libmanagement.repositories.StaffRepo;
import com.example.intern_vnpttech_libmanagement.security.jwt.JWTProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

@Service
@Slf4j
public class SecurityService {

    @Autowired
    private JWTProvider jwtProvider;

    @Autowired
    private StaffRepo staffRepo;

    public Staff getStaffFromRequest(HttpServletRequest request)
    {
        try{
            String token = jwtProvider.getTokenFromRequest(request);
            Integer staffId = jwtProvider.getStaffIdFromToken(token);
            return staffRepo.findById(staffId).isPresent()
                    ? staffRepo.findById(staffId).get()
                    :null;
        } catch (Exception ex)
        {
            log.error("Get staff from token error",ex);
            return null;
        }
    }
}
