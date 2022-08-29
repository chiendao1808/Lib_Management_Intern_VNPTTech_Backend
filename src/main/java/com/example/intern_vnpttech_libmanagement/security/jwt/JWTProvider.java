package com.example.intern_vnpttech_libmanagement.security.jwt;

import com.example.intern_vnpttech_libmanagement.constants.SecurityConstants;
import com.example.intern_vnpttech_libmanagement.security.user_details.CustomUserDetails;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
public class JWTProvider {

    // generate a jwt from userdetail
   public String generateToken(CustomUserDetails userDetails)
    {
        Date now = new Date(System.currentTimeMillis());
        Map<String, Object> claims = new HashMap<>();
        claims.put("staffUsername",userDetails.getStaff().getStaffUsername());
        claims.put("staffId",userDetails.getStaff().getStaffId());
        return Jwts.builder().setSubject(userDetails.getStaff().getStaffUsername())
                            .setClaims(claims)
                            .setIssuedAt(now)
                            .setExpiration(new Date(now.getTime()+ SecurityConstants.EXPERITION_TIME))
                            .signWith(SignatureAlgorithm.HS512,SecurityConstants.SECURITY_KEY)
                            .compact();
    }

   public Claims getClaims(String token)
    {
        try{
            Claims claims = Jwts.parser().setSigningKey(SecurityConstants.SECURITY_KEY).parseClaimsJws(token).getBody();
            return claims;
        } catch (Exception ex)
        {
            log.error("get claims error",ex);
            return null;
        }
    }

    public String getUsernameFromToken(String token)
    {
        try{
            Claims claims = getClaims(token);
            String staffUsername = claims.get("staffUsername").toString();
            return staffUsername;
        } catch (Exception ex)
        {
            log.error("Get staffUsername from token error",ex);
            return null;
        }
    }

    //get staffId from a token
    public Integer getStaffIdFromToken(String token)
    {
        try{
            return (Integer) getClaims(token).get("staffId");
        } catch (Exception ex)
        {
            log.error("Get staffId from token error",ex);
            return null;
        }
    }

    //check if token is expired
   public boolean isExpired(String token)
    {
        try{
            Claims claims = getClaims(token);
            Date now = new Date(System.currentTimeMillis());
            return claims.getExpiration().after(now)?false:true;
        } catch (Exception ex)
        {
            log.error("Check token expired fail");
            return true;
        }
    }

    // validate token function
    public boolean validateToken(String token)
    {
        try{
            return isExpired(token)?false:true;
        } catch (Exception ex)
        {
            log.error("validate token error",ex);
            ex.printStackTrace();
            return false;
        }
    }

    // get a token from a request sent
    public String getTokenFromRequest(HttpServletRequest request)
    {
      try{
          String authHeader = request.getHeader(SecurityConstants.AUTH_HEADER);
          if(StringUtils.hasText(authHeader) && authHeader.contains(SecurityConstants.TOKEN_TYPE)
          && !request.getRequestURI().contains("/auth/"))
              return authHeader.substring(SecurityConstants.TOKEN_TYPE.length()+1);
          else  return null;
      } catch (Exception ex)
      {
          log.error("Get token from request fail");
          return null;
      }
    }
}
