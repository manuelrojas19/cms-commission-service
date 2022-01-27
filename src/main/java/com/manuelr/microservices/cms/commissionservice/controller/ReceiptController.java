package com.manuelr.microservices.cms.commissionservice.controller;

import com.manuelr.microservices.cms.commissionservice.dto.ReceiptDto;
import com.manuelr.microservices.cms.commissionservice.service.ReceiptService;
import lombok.AllArgsConstructor;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@RequestMapping("/api/v1")
public class ReceiptController {
    private final ReceiptService receiptService;

    private static final String DEFAULT_PAGE_NUMBER = "0";
    private static final String DEFAULT_PAGE_SIZE = "8";

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/receipts")
    public ResponseEntity<CollectionModel<ReceiptDto>> findAll(
            @RequestParam(name = "page", required = false, defaultValue = DEFAULT_PAGE_NUMBER) Integer page,
            @RequestParam(name = "size", required = false, defaultValue = DEFAULT_PAGE_SIZE) Integer size) {
        CollectionModel<ReceiptDto> response = receiptService.findAll(page, size);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('EMPLOYEE') or hasAuthority('MANAGER')")
    @GetMapping("/commissions/{commissionId}/receipts")
    public ResponseEntity<CollectionModel<ReceiptDto>> findAllByEmployeeId(
            @RequestParam(name = "page", required = false, defaultValue = DEFAULT_PAGE_NUMBER) Integer page,
            @RequestParam(name = "size", required = false, defaultValue = DEFAULT_PAGE_SIZE) Integer size,
            @PathVariable String commissionId) {
        CollectionModel<ReceiptDto> response = receiptService.findAllByCommission(commissionId, page, size);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('EMPLOYEE')")
    @PostMapping("/receipts")
    public ResponseEntity<ReceiptDto> create(@Validated @RequestBody ReceiptDto request) {
        ReceiptDto response = receiptService.create(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
}
