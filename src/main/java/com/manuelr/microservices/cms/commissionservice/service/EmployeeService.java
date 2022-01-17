package com.manuelr.microservices.cms.commissionservice.service;

import com.manuelr.microservices.cms.commissionservice.entity.Employee;

public interface EmployeeService {
    Employee findByPersonId(Long personId);
    Employee findCurrentEmployeeUser();
}
