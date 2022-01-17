package com.manuelr.microservices.cms.commissionservice.entity;

import lombok.*;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Document(collection = "employees")
public class Employee extends Person {
    @DBRef
    private Manager manager;

    @DBRef(lazy = true)
    private List<Commission> commissions;
}
