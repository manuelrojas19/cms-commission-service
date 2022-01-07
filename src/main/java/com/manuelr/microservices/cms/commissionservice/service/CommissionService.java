package com.manuelr.microservices.cms.commissionservice.service;

import com.manuelr.cms.commons.dto.CommissionDto;
import org.springframework.hateoas.CollectionModel;

public interface CommissionService {
    CollectionModel<CommissionDto> findAll(Integer page, Integer size);
    CollectionModel<CommissionDto> findAllByEmployeeId(Long employeeId, Integer page, Integer size);
    CommissionDto findById(String id);
    CommissionDto create(CommissionDto commissionDto);
    CommissionDto update(CommissionDto commissionDto, String id);
    void delete(String id);
}
