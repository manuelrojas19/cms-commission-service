package com.manuelr.microservices.cms.commissionservice.dto;

import lombok.*;
import org.springframework.hateoas.RepresentationModel;

import java.time.LocalDateTime;
import java.util.Map;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class ErrorResponseDto extends RepresentationModel<ErrorResponseDto> {
    private Integer statusCode;
    private String message;
    private String developerMessage;
    private Map<String, String> params;
    private LocalDateTime timestamp;
}
