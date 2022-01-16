package com.manuelr.microservices.cms.commissionservice.repository;

import com.manuelr.microservices.cms.commissionservice.entity.Commission;
import com.manuelr.microservices.cms.commissionservice.entity.Employee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreFilter;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface CommissionRepository extends MongoRepository<Commission, String> {

    @PostAuthorize("(hasAuthority('MANAGER') " +
            "and (returnObject.empty ? true : (returnObject.get()).employee.manager.personId == authentication.principal.personId)) " +
            "or (hasAuthority('EMPLOYEE') " +
            "and (returnObject.empty ? true : (returnObject.get()).employee.personId == authentication.principal.personId))")
    Optional<Commission> findCommissionById(String id);

    @Query(value = " { $and: [" +
            "{ 'employeeId': ?0 }," +
            "{ $or: [" +
            "{ 'beginDate' : { '$gte' : ?1, '$lt': ?2 } }," +
            "{ 'endDate': { '$gte': ?1, '$lt': ?2 } }" +
            "] } ] }",
            count = true)
    Long existsOverlappedCommissions(Long employeeId, LocalDate beginDate, LocalDate endDate);


    List<Commission> findAllByEmployee(Employee employee);

    Page<Commission> findAllByEmployee(Employee employee, Pageable pageable);

}
