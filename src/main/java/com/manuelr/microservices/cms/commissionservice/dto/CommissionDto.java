package com.manuelr.microservices.cms.commissionservice.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.manuelr.microservices.cms.commissionservice.entity.CommissionPlace;
import com.manuelr.microservices.cms.commissionservice.entity.CommissionType;
import lombok.*;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Relation(collectionRelation = "commissions")
public class CommissionDto extends RepresentationModel<CommissionDto> {
    @Null
    private String id;
    @Null
    private Long version;
    @NotNull
    private Long employeeId;
    @NotNull
    private CommissionType type;
    @NotNull
    private CommissionPlace place;
    @NotNull
    @JsonFormat(pattern="yyyy-MM-dd", shape=JsonFormat.Shape.STRING)
    private LocalDate beginDate;
    @NotNull
    @JsonFormat(pattern="yyyy-MM-dd", shape=JsonFormat.Shape.STRING)
    private LocalDate endDate;
    @Null
    @JsonFormat(shape=JsonFormat.Shape.STRING)
    private BigDecimal assignedAmount;
    @Null
    private Boolean isApproved;
    @Null
    private LocalDateTime createdAt;
    @Null
    private LocalDateTime updatedAt;
}
