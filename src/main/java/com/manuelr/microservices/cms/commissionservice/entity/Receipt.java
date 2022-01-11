package com.manuelr.microservices.cms.commissionservice.entity;

import com.manuelr.microservices.cms.commissionservice.enums.ReceiptType;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@Document(collection = "receipts")
public class Receipt {
    @Id
    private String id;

    @DBRef
    private Commission commission;

    @NotNull
    private LocalDate date;

    @NotNull
    private ReceiptType type;

    @NotNull
    private BigDecimal amount;

    @NotNull
    private String documentUrl;

    @Version
    private Long version;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;
}
