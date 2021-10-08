package com.manuelr.microservices.cms.commissionservice.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CommissionPlace {
    private String city;
    private String state;
    private String country;
}
