package com.manuelr.microservices.cms.commissionservice.proxy;

import com.manuelr.cms.commons.dto.EmployeeDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


@FeignClient("employees-service")
public interface EmployeeServiceFeignClient {
    @GetMapping("/api/v1/employees/{id}")
    ResponseEntity<EmployeeDto> findById(@PathVariable Long id);

    @GetMapping("/api/v1/employees/current")
    ResponseEntity<EmployeeDto> findCurrentEmployeeUser();
}
