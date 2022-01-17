package com.manuelr.microservices.cms.commissionservice.repository;

import com.manuelr.microservices.cms.commissionservice.entity.Manager;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface ManagerRepository extends MongoRepository<Manager, String> {
    Optional<Manager> findByPersonId(Long id);
}
