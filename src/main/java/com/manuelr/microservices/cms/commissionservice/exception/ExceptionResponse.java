package com.manuelr.microservices.cms.commissionservice.exception;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.security.NoSuchAlgorithmException;

@Getter
@Setter
@Builder
public class ExceptionResponse {
    String message;
}
