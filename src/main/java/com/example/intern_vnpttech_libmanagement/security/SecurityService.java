package com.example.intern_vnpttech_libmanagement.security;

import com.example.intern_vnpttech_libmanagement.entities.LibStaff;
import com.example.intern_vnpttech_libmanagement.repositories.LibStaffRepo;
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
    private LibStaffRepo libStaffRepo;

    public LibStaff getStaffFromRequest(HttpServletRequest request)
    {
        try{
            String token = jwtProvider.getTokenFromRequest(request);
            Integer staffId = jwtProvider.getStaffIdFromToken(token);
            return libStaffRepo.findById(staffId).isPresent()
                    ?libStaffRepo.findById(staffId).get()
                    :null;
        } catch (Exception ex)
        {
            log.error("Get staff from token error",ex);
            return null;
        }
    }
}
