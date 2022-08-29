package com.example.intern_vnpttech_libmanagement.controllers;

import com.example.intern_vnpttech_libmanagement.dto.request.LoginRequest;
import com.example.intern_vnpttech_libmanagement.dto.response.LoginResponse;
import com.example.intern_vnpttech_libmanagement.dto.response.MessageResponse;
import com.example.intern_vnpttech_libmanagement.entities.LibStaff;
import com.example.intern_vnpttech_libmanagement.security.jwt.JWTProvider;
import com.example.intern_vnpttech_libmanagement.security.user_details.CustomUserDetailService;
import com.example.intern_vnpttech_libmanagement.security.user_details.CustomUserDetails;
import com.example.intern_vnpttech_libmanagement.services.LibStaffService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.security.RolesAllowed;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@RestController
@RequestMapping("libmng/api/auth")
@Tag(name = "Authentication Controller")
public class AuthController {

    @Autowired
    private LibStaffService libStaffService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authentication;

    @Autowired
    private CustomUserDetailService userDetailService;

    @Autowired
    private JWTProvider jwtProvider;

    @Operation(summary = "Sign up a library staff")
    @PostMapping(path = "/signup")
    public ResponseEntity<?> signUp(@RequestBody LibStaff staff)
    {
        return libStaffService.add(staff).isPresent()
                ?ResponseEntity.status(200).body(new MessageResponse("Add staff successfully","success"))
                :ResponseEntity.status(200).body(new MessageResponse("Add staff fail","fail"));
    }

    @Operation(summary = "The staff signin to system to get access token to use other APIs")
    @PostMapping("/signin")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest,
                                   HttpServletRequest request)
    {
        try{
            Authentication auth = authentication.authenticate(new UsernamePasswordAuthenticationToken(
                    loginRequest.getUsername(),loginRequest.getPassword()));
            SecurityContextHolder.getContext().setAuthentication(auth);
            CustomUserDetails userDetails =(CustomUserDetails)userDetailService.loadUserByUsername(loginRequest.getUsername());
            if(userDetails==null)
                throw new BadCredentialsException("Wrong username/password");
            String accessToken = jwtProvider.generateToken(userDetails);
            if(accessToken==null || accessToken.isEmpty())
                return ResponseEntity.status(200).body(new MessageResponse("Generate token fail","fail"));
            return ResponseEntity.status(200).body(new LoginResponse(accessToken,"JWT",new Date()));
        } catch (Exception ex)
        {
            ex.printStackTrace();
            return ResponseEntity.badRequest().body(new MessageResponse("Authenticate fail","fail"));
        }
    }

}
