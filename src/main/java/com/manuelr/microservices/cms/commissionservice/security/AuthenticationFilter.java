package com.manuelr.microservices.cms.commissionservice.security;

import lombok.extern.slf4j.Slf4j;
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
public class AuthenticationFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        if (Objects.isNull(request.getHeader("X-Auth-UserId"))
                || Objects.isNull(request.getHeader("X-Auth-UserRole"))) {
            response.setStatus(HttpStatus.FORBIDDEN.value());
            return;
        }

        log.info("Role ---> {}",request.getHeader("X-Auth-UserRole"));

        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(request.getHeader("X-Auth-UserId"), null,
                        Collections.singletonList(new SimpleGrantedAuthority(request.getHeader("X-Auth-UserRole"))));
        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        log.info("Authentication object ---> {}", authentication);

        filterChain.doFilter(request, response);
    }
}
