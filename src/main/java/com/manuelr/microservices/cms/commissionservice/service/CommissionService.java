package com.manuelr.microservices.cms.commissionservice.service;

import com.manuelr.microservices.cms.commissionservice.dto.CommissionDto;
import org.springframework.hateoas.CollectionModel;

public interface CommissionService {
    CollectionModel<CommissionDto> findAllCommissions(Integer page, Integer size);
    CollectionModel<CommissionDto> findCommissionsByEmployeeId(Long employeeId, Integer page, Integer size);
    CommissionDto findCommissionById(String id);
    CommissionDto createCommission(CommissionDto commissionDto);
    void deleteCommission(String id);
}
