package com.manuelr.microservices.cms.commissionservice.exception;

public class OverlapDatesException extends RuntimeException {
    public OverlapDatesException(String s) {
        super(s);
    }
}
