package com.manuelr.microservices.cms.commissionservice.service;

import com.manuelr.cms.commons.dto.EmployeeDto;

public interface EmployeeService {
    EmployeeDto findById(Long id);
    EmployeeDto findCurrentEmployeeUser();
}
