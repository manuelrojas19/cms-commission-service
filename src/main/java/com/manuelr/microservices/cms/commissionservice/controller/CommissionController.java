package com.manuelr.microservices.cms.commissionservice.controller;

import com.manuelr.cms.commons.dto.CommissionDto;
import com.manuelr.microservices.cms.commissionservice.service.CommissionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/v1")
public class CommissionController {
    @Autowired
    private CommissionService commissionService;

    private static final String DEFAULT_PAGE_NUMBER = "0";
    private static final String DEFAULT_PAGE_SIZE = "8";

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/commissions")
    public ResponseEntity<CollectionModel<CommissionDto>> findAll(
            @RequestParam(name = "page", required = false, defaultValue = DEFAULT_PAGE_NUMBER) Integer page,
            @RequestParam(name = "size", required = false, defaultValue = DEFAULT_PAGE_SIZE) Integer size) {
        CollectionModel<CommissionDto> response = commissionService.findAll(page, size);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('EMPLOYEE')")
    @GetMapping("/employees/current/commissions")
    public ResponseEntity<CollectionModel<CommissionDto>> findAllByCurrentEmployee(
            @RequestParam(name = "page", required = false, defaultValue = DEFAULT_PAGE_NUMBER) Integer page,
            @RequestParam(name = "size", required = false, defaultValue = DEFAULT_PAGE_SIZE) Integer size) {
        CollectionModel<CommissionDto> response = commissionService
                .findAllByCurrentEmployeeUser(page, size);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('MANAGER') or hasAuthority('EMPLOYEE')")
    @GetMapping("/commissions/{id}")
    public ResponseEntity<CommissionDto> findById(@PathVariable String id) {
        CommissionDto response = commissionService.findById(id);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('MANAGER') or" +
            "(hasAuthority('EMPLOYEE') and #id == authentication.principal.personId)")
    @GetMapping("/employees/{id}/commissions")
    public ResponseEntity<CollectionModel<CommissionDto>> findAllByEmployee(
            @RequestParam(name = "page", required = false, defaultValue = DEFAULT_PAGE_NUMBER) Integer page,
            @RequestParam(name = "size", required = false, defaultValue = DEFAULT_PAGE_SIZE) Integer size,
            @PathVariable Long id) {
        CollectionModel<CommissionDto> response = commissionService.findAllByEmployee(id, page, size);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('EMPLOYEE')")
    @PostMapping("/commissions")
    public ResponseEntity<CommissionDto> create(@Validated @RequestBody CommissionDto request) {
        log.info("Received data ---> {}", request);
        CommissionDto response = commissionService.create(request);
        log.info("Sending data to the client ---> {}", response);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PreAuthorize("hasAuthority('MANAGER')")
    @PutMapping("/commissions/{id}/approveByManager")
    public ResponseEntity<Void> approveByManager(@PathVariable String id) {
        commissionService.approveByManager(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping("/commissions/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        commissionService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
