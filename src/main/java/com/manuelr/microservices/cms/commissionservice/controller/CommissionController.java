package com.manuelr.microservices.cms.commissionservice.controller;

import com.manuelr.microservices.cms.commissionservice.dto.CommissionDto;
import com.manuelr.microservices.cms.commissionservice.service.CommissionService;
import lombok.AllArgsConstructor;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@RequestMapping("/api/v1")
public class CommissionController {
    private final CommissionService commissionService;

    private static final String DEFAULT_PAGE_NUMBER = "0";
    private static final String DEFAULT_PAGE_SIZE = "8";

    @GetMapping("/commissions")
    public ResponseEntity<CollectionModel<CommissionDto>> findAllCommissions(
            @RequestParam(name = "page", required = false, defaultValue = DEFAULT_PAGE_NUMBER) Integer page,
            @RequestParam(name = "size", required = false, defaultValue = DEFAULT_PAGE_SIZE) Integer size) {
        CollectionModel<CommissionDto> response = commissionService.findAllCommissions(page, size);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/employees/{id}/commissions")
    public ResponseEntity<CollectionModel<CommissionDto>> findCommissionsByEmployeeId(
            @RequestParam(name = "page", required = false, defaultValue = DEFAULT_PAGE_NUMBER) Integer page,
            @RequestParam(name = "size", required = false, defaultValue = DEFAULT_PAGE_SIZE) Integer size,
            @PathVariable Long id) {
        CollectionModel<CommissionDto> response = commissionService.findCommissionsByEmployeeId(id, page, size);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/commissions/{id}")
    public ResponseEntity<CommissionDto> findCommissionById(@PathVariable String id) {
        CommissionDto response = commissionService.findCommissionById(id);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/commissions")
    public ResponseEntity<CommissionDto> createCommission(@Validated @RequestBody CommissionDto request) {
        CommissionDto response = commissionService.createCommission(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @DeleteMapping("/commissions/{id}")
    public ResponseEntity<Void> deleteCommission(@PathVariable String id) {
        commissionService.deleteCommission(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
