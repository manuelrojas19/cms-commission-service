package com.manuelr.microservices.cms.commissionservice.repository;

import com.manuelr.microservices.cms.commissionservice.entity.Employee;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.security.access.prepost.PostAuthorize;

import java.util.Optional;

public interface EmployeeRepository extends MongoRepository<Employee, String> {

    @PostAuthorize("(hasAuthority('MANAGER') " +
            "and returnObject.empty ? true : (returnObject.get()).manager.personId == authentication.principal.personId) " +
            "or (hasAuthority('EMPLOYEE') " +
            "and returnObject.empty ? true : (returnObject.get()).personId == authentication.principal.personId)")
    Optional<Employee> findByPersonId(Long id);
}
