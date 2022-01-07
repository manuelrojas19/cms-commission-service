package com.manuelr.microservices.cms.commissionservice.controller;

import com.manuelr.cms.commons.dto.CommissionDto;
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
    public ResponseEntity<CollectionModel<CommissionDto>> findAll(
            @RequestParam(name = "page", required = false, defaultValue = DEFAULT_PAGE_NUMBER) Integer page,
            @RequestParam(name = "size", required = false, defaultValue = DEFAULT_PAGE_SIZE) Integer size) {
        CollectionModel<CommissionDto> response = commissionService.findAll(page, size);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/employees/{id}/commissions")
    public ResponseEntity<CollectionModel<CommissionDto>> findAllByEmployeeId(
            @RequestParam(name = "page", required = false, defaultValue = DEFAULT_PAGE_NUMBER) Integer page,
            @RequestParam(name = "size", required = false, defaultValue = DEFAULT_PAGE_SIZE) Integer size,
            @PathVariable Long id) {
        CollectionModel<CommissionDto> response = commissionService.findAllByEmployeeId(id, page, size);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    @GetMapping("/commissions/{id}")
    public ResponseEntity<CommissionDto> findById(@PathVariable String id) {
        CommissionDto response = commissionService.findById(id);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/commissions")
    public ResponseEntity<CommissionDto> create(@Validated @RequestBody CommissionDto request) {
        CommissionDto response = commissionService.create(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping("/commissions/{id}")
    public ResponseEntity<CommissionDto> update(@RequestBody CommissionDto commissionDto, @PathVariable String id) {
        CommissionDto response = commissionService.update(commissionDto, id);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/commissions/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        commissionService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
