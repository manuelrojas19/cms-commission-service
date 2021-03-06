package com.manuelr.microservices.cms.commissionservice.entity;

import com.manuelr.cms.commons.enums.CommissionType;
import com.manuelr.cms.commons.utils.Place;
import com.manuelr.microservices.cms.commissionservice.enums.ApprovalStatus;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@Document(collection = "commissions")
public class Commission {
    @Id
    private String id;

    @DBRef
    private Employee employee;

    @NotNull
    private CommissionType type;

    @NotNull
    private Place place;

    @NotNull
    private LocalDate beginDate;

    @NotNull
    private LocalDate endDate;

    @Null
    private ApprovalStatus managerApprovalStatus = ApprovalStatus.PENDING;

    @Null
    private BigDecimal assignedAmount;

    @DBRef(lazy = true)
    private List<Receipt> receipts;

    @Version
    private Long version;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;
}
