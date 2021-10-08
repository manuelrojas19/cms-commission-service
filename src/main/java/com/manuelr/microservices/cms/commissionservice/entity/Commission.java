package com.manuelr.microservices.cms.commissionservice.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "commissions")
public class Commission {
    @Id
    private String id;
    @Version
    private Long version;
    @NotNull
    private Long employeeId;
    @NotNull
    private CommissionType type;
    @NotNull
    private CommissionPlace place;
    @NotNull
    private LocalDate beginDate;
    @NotNull
    private LocalDate endDate;
    @Null
    private BigDecimal assignedAmount;
    @Null
    private Boolean isApproved;
    @CreatedDate
    private LocalDateTime createdAt;
    @LastModifiedDate
    private LocalDateTime updatedAt;
}
