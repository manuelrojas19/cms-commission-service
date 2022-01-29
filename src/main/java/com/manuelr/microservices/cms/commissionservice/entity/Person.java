package com.manuelr.microservices.cms.commissionservice.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;

@Getter
@Setter
@ToString
public abstract class Person {
    @Id
    private String id;

    @Indexed(unique=true)
    private Long personId;
}
