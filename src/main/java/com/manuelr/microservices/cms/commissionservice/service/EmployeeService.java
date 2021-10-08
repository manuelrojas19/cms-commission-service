package com.manuelr.microservices.cms.commissionservice.service;

import com.manuelr.microservices.cms.commissionservice.dto.EmployeeDto;

public interface EmployeeService {
    EmployeeDto findEmployeeById(Long id);
}
