package com.manuelr.microservices.cms.commissionservice.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;


@Getter
@Setter
@ToString
public abstract class Person {
    @Id
    private String id;

    private Long personId;
}
