package com.manuelr.microservices.cms.commissionservice.service.impl;

import com.manuelr.cms.commons.security.UserData;
import com.manuelr.microservices.cms.commissionservice.entity.Employee;
import com.manuelr.microservices.cms.commissionservice.exception.NotFoundException;
import com.manuelr.microservices.cms.commissionservice.repository.EmployeeRepository;
import com.manuelr.microservices.cms.commissionservice.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Override
    public Employee findByPersonId(Long personId) {
        return employeeRepository.findByPersonId(personId)
                .orElseThrow(() -> new NotFoundException("Employee not found", personId.toString()));
    }

    @Override
    public Employee findCurrentEmployeeUser() {
        Long personId = ((UserData) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getPersonId();
        return employeeRepository.findByPersonId(personId)
                .orElseThrow(() -> new NotFoundException("Employee not found", personId.toString()));
    }
}
