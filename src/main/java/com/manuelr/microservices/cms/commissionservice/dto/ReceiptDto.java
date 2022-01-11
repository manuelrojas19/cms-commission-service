package com.manuelr.microservices.cms.commissionservice.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.manuelr.microservices.cms.commissionservice.enums.ReceiptType;
import lombok.*;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Relation(collectionRelation = "receipts")
public class ReceiptDto extends RepresentationModel<ReceiptDto> {
    private String id;
    private String commissionId;

    @NotNull
    @JsonFormat(pattern = "yyyy-MM-dd", shape = JsonFormat.Shape.STRING)
    private LocalDate receiptDate;

    private ReceiptType type;
    private BigDecimal amount;
    private String documentUrl;
    private Long version;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
