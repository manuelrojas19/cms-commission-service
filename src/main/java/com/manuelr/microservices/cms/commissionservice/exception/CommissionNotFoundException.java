package com.manuelr.microservices.cms.commissionservice.exception;

import lombok.Getter;

@Getter
public class CommissionNotFoundException extends RuntimeException {
    private static final String DEV_MESSAGE = "Commission with Id %s was not found";

    private String commissionId;
    private String developerMessage;

    public CommissionNotFoundException(String message, String commissionId) {
        super(message);
        this.commissionId = commissionId;
        this.developerMessage = String.format(DEV_MESSAGE, commissionId);
    }
}
