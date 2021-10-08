package com.manuelr.microservices.cms.commissionservice.dto;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class EmployeeDto {
    private Long id;
    private Long version;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private LocalDate birthday;
    private String email;
    private Gender gender;
    private Position position;
}
