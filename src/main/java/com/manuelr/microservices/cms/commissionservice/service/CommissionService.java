package com.manuelr.microservices.cms.commissionservice.service;

import com.manuelr.cms.commons.dto.CommissionDto;
import org.springframework.hateoas.CollectionModel;

public interface CommissionService {
    CollectionModel<CommissionDto> findAll(Integer page, Integer size);

    CollectionModel<CommissionDto> findAllByEmployee(Long employeeId, Integer page, Integer size);

    CollectionModel<CommissionDto> findAllByCurrentEmployeeUser(Integer page, Integer size);

    CommissionDto findById(String id);

    CommissionDto create(CommissionDto commissionDto);

    void delete(String id);

    void approveByManager(String id);
}
