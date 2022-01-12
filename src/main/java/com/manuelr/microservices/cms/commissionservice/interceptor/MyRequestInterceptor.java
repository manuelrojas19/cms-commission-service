package com.manuelr.microservices.cms.commissionservice.interceptor;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

@Component
public class MyRequestInterceptor implements RequestInterceptor {

    @Autowired
    HttpServletRequest request;

    @Override
    public void apply(RequestTemplate template) {
        template.header("X-Auth-Token", request.getHeader("X-Auth-Token"));
    }
}