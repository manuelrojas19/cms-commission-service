package com.manuelr.microservices.cms.commissionservice.service;

import com.manuelr.cms.commons.dto.CommissionDto;
import org.springframework.hateoas.CollectionModel;
import org.springframework.security.access.prepost.PostAuthorize;

public interface CommissionService {
    CollectionModel<CommissionDto> findAll(Integer page, Integer size);

    CollectionModel<CommissionDto> findAllByEmployeeId(Long employeeId, Integer page, Integer size);

    CollectionModel<CommissionDto> findAllByCurrentEmployeeUser(Integer page, Integer size);

    @PostAuthorize("hasAuthority('MANAGER')" +
            "or hasAuthority('FINANCE')" +
            "or (hasAuthority('EMPLOYEE') and returnObject.body.employeeId == authentication.principal.personId)")
    CommissionDto findById(String id);

    CommissionDto create(CommissionDto commissionDto);

    CommissionDto update(CommissionDto commissionDto, String id);

    void delete(String id);
}
