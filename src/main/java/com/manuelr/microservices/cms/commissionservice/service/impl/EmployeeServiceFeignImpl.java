package com.manuelr.microservices.cms.commissionservice.service.impl;

import com.manuelr.cms.commons.dto.EmployeeDto;
import com.manuelr.microservices.cms.commissionservice.exception.BadRequestException;
import com.manuelr.microservices.cms.commissionservice.exception.NotFoundException;
import com.manuelr.microservices.cms.commissionservice.service.EmployeeFeignService;
import com.manuelr.microservices.cms.commissionservice.web.proxy.EmployeeServiceFeignClient;
import feign.FeignException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Slf4j
@AllArgsConstructor
@Service
public class EmployeeServiceFeignImpl implements EmployeeFeignService {
    private final EmployeeServiceFeignClient employeeServiceFeignClient;

    @Override
    public EmployeeDto findById(Long id) {
        EmployeeDto employeeDto;
        try {
            log.info("Calling Employee Service ---> employeeId {}", id);
            ResponseEntity<EmployeeDto> responseEntity = employeeServiceFeignClient.findById(id);
            log.debug("Employee --> {}", responseEntity.getBody());
            employeeDto = responseEntity.getBody();
        } catch (FeignException e) {
            if (!(e.status() == HttpStatus.NOT_FOUND.value()))
                throw new BadRequestException(e.getMessage());
            throw new NotFoundException("Employee was not found", id.toString());
        }
        return employeeDto;
    }

    @Override
    public EmployeeDto findCurrentEmployeeUser() {
        EmployeeDto employeeDto;
        try {
            log.info("Calling Employee Service ---> currentEmployee ");
            ResponseEntity<EmployeeDto> responseEntity = employeeServiceFeignClient.findCurrentEmployeeUser();
            log.debug("Employee --> {}", responseEntity.getBody());
            employeeDto = responseEntity.getBody();
        } catch (FeignException e) {
            if (!(e.status() == HttpStatus.NOT_FOUND.value()))
                throw new BadRequestException(e.getMessage());
            throw new NotFoundException("Employee was not found", null);
        }
        return employeeDto;
    }


}
