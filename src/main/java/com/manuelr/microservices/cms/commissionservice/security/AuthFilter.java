package com.manuelr.microservices.cms.commissionservice.security;

import com.manuelr.cms.commons.security.SecurityCipher;
import com.manuelr.cms.commons.security.UserData;
import com.manuelr.microservices.cms.commissionservice.util.JwtTokenUtil;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.Objects;

@Slf4j
public class AuthFilter extends OncePerRequestFilter {
    private static final String AUTH_HEADER_NAME = "X-Auth-Token";

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain)
            throws ServletException, IOException {

        if (Objects.isNull(request.getHeader(AUTH_HEADER_NAME))) {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            return;
        }
        log.info("Encrypted Token ---> {}", request.getHeader(AUTH_HEADER_NAME));
        String token = SecurityCipher.decrypt(request.getHeader(AUTH_HEADER_NAME));

        String username = jwtTokenUtil.getUsernameFromToken(token);

        UserData userData = jwtTokenUtil.getUserDataFromToken(token);
        userData.setEmail(username);

        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(userData, null,
                        Collections.singletonList(new SimpleGrantedAuthority(userData.getRole().name())));
        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        filterChain.doFilter(request, response);
    }
}
