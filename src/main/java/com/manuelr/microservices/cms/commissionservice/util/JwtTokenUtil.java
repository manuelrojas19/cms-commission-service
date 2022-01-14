package com.manuelr.microservices.cms.commissionservice.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.manuelr.cms.commons.security.UserData;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class JwtTokenUtil {

    @Value("${authentication-dev.auth.tokenSecret}")
    private String tokenSecret;

    public String getUsernameFromToken(String token) {
        log.info("Token ---> {}", token);
        Claims claims = Jwts.parser().setSigningKey(tokenSecret).parseClaimsJws(token).getBody();
        return claims.getSubject();
    }


    public UserData getUserDataFromToken(String token) {
        ObjectMapper mapper = new ObjectMapper();
        Claims claims = Jwts.parser().setSigningKey(tokenSecret).parseClaimsJws(token).getBody();
        return mapper.convertValue(claims.get("userData", Object.class), UserData.class);
    }

}
