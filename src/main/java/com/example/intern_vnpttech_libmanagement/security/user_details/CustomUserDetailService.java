package com.example.intern_vnpttech_libmanagement.security.user_details;

import com.example.intern_vnpttech_libmanagement.repositories.StaffRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class CustomUserDetailService implements UserDetailsService {

    @Autowired
    private StaffRepo staffRepo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        try{
            if(!staffRepo.findByUsernameOrEmail(username,"").isPresent())
                throw new UsernameNotFoundException("Staff not found");
            return new CustomUserDetails(staffRepo.findByUsernameOrEmail(username,"").get());
        } catch (Exception ex)
        {
          log.error("Load staff error",ex);
          return null;
        }
    }
}
