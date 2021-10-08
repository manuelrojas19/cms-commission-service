package com.manuelr.microservices.cms.commissionservice.repository;

import com.manuelr.microservices.cms.commissionservice.entity.Commission;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.time.LocalDate;
import java.util.Optional;

public interface CommissionRepository extends MongoRepository<Commission, String> {
    Optional<Commission> findCommissionById(String id);

    @Query(value = " { $and: [" +
            "{ 'employeeId': ?0 }," +
            "{ $or: [" +
            "{ 'beginDate' : { '$gte' : ?1, '$lt': ?2 } }," +
            "{ 'endDate': { '$gte': ?1, '$lt': ?2 } }" +
            "] } ] }",
            count = true)
    Long existsOverlappedCommissions(Long employeeId, LocalDate beginDate, LocalDate endDate);

    Page<Commission> findAllByEmployeeId(Long employeeId, Pageable pageable);
}
