package com.manuelr.microservices.cms.commissionservice.service.impl;

import com.manuelr.microservices.cms.commissionservice.dto.EmployeeDto;
import com.manuelr.microservices.cms.commissionservice.service.EmployeeService;
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
public class EmployeeServiceImpl implements EmployeeService {
    private final EmployeeServiceFeignClient employeeServiceFeignClient;

    @Override
    public EmployeeDto findEmployeeById(Long id) {
        EmployeeDto employeeDto = null;
        try {
            log.info("Calling Employee Service ---> employeeId {}", id);
            ResponseEntity<EmployeeDto> responseEntity = employeeServiceFeignClient.findEmployeeById(id);
            log.debug("Employee --> {}", responseEntity.getBody());
            employeeDto = responseEntity.getBody();
        } catch (FeignException e) {
            log.info("Employee was not found");
            if (!(e.status() == HttpStatus.NOT_FOUND.value()))
                throw new RuntimeException(e);
        }
        return employeeDto;
    }

}
