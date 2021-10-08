package com.manuelr.microservices.cms.commissionservice.controller;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import com.manuelr.microservices.cms.commissionservice.dto.ErrorResponseDto;
import com.manuelr.microservices.cms.commissionservice.exception.CommissionNotFoundException;
import com.manuelr.microservices.cms.commissionservice.exception.EmployeeNotFoundException;
import com.manuelr.microservices.cms.commissionservice.exception.ExceptionResponse;
import com.manuelr.microservices.cms.commissionservice.exception.OverlapDatesException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.stream.Collectors;

@ControllerAdvice
@Slf4j
public class MvcExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> validationErrorHandler(MethodArgumentNotValidException e) {
        Map<String, String> errorList = e.getFieldErrors()
                .stream().collect(Collectors
                        .toMap(FieldError::getField, DefaultMessageSourceResolvable::getDefaultMessage));
        log.info("Sending validationError response to the client");

        return new ResponseEntity<>(errorList, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ExceptionResponse> runtimeExceptionHandler(RuntimeException e) {
        e.printStackTrace();
        ExceptionResponse response = ExceptionResponse.builder().message(e.getMessage()).build();
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(EmployeeNotFoundException.class)
    public ResponseEntity<ExceptionResponse> employeeNotFoundHandler(EmployeeNotFoundException e) {
        ExceptionResponse response = ExceptionResponse.builder().message(e.getMessage()).build();
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(CommissionNotFoundException.class)
    public ResponseEntity<ErrorResponseDto> commissionNotFoundHandler(CommissionNotFoundException e) {
        ErrorResponseDto response = ErrorResponseDto.builder()
                .statusCode(HttpStatus.NOT_FOUND.value())
                .message(e.getMessage())
                .developerMessage(e.getDeveloperMessage())
                .timestamp(LocalDateTime.now())
                .build();
        Map<String, String> params = Map.of("commissionId", e.getCommissionId());
        response.setParams(params);
        response.add(WebMvcLinkBuilder.linkTo(methodOn(CommissionController.class).findCommissionById(e.getCommissionId())).withSelfRel());
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(OverlapDatesException.class)
    public ResponseEntity<ExceptionResponse> overlapDatesHandler(OverlapDatesException e) {
        ExceptionResponse response = ExceptionResponse.builder().message(e.getMessage()).build();
        return new ResponseEntity<>(response, HttpStatus.CONFLICT);
    }


}
