package com.example.intern_vnpttech_libmanagement.security.jwt;

import com.example.intern_vnpttech_libmanagement.security.user_details.CustomUserDetailService;
import com.example.intern_vnpttech_libmanagement.security.user_details.CustomUserDetails;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@Slf4j
public class JWTAuthFilter extends OncePerRequestFilter {

    @Autowired
    private JWTProvider jwtProvider;

    @Autowired
    private CustomUserDetailService customUserDetailService;

    // Define a filter for jwt filter
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try{
            String token = jwtProvider.getTokenFromRequest(request);
            if(StringUtils.hasText(token) && jwtProvider.validateToken(token) && !request.getRequestURI().contains("auth"))
            {
                String staffUserName = jwtProvider.getUsernameFromToken(token);
                CustomUserDetails userDetails = (CustomUserDetails) customUserDetailService.loadUserByUsername(staffUserName);
                if(userDetails!=null)
                {
                    UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());
                    auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(auth);
                }
                else throw new RuntimeException("Can't load userdetails");
            }
        }  catch (Exception ex)
        {
            log.error("Unable to authenticate staff",ex);
        }
        filterChain.doFilter(request,response);
    }
}
