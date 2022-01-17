package com.manuelr.microservices.cms.commissionservice.entity;

import lombok.*;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Document(collection = "managers")
public class Manager extends Person {

    @DBRef(lazy = true)
    private List<Employee> subordinates;
}
