package com.manuelr.microservices.cms.commissionservice.exception;

public class BadRequestException extends RuntimeException {
    public BadRequestException(String s) {
        super(s);
    }
}
