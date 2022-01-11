package com.manuelr.microservices.cms.commissionservice.web.assembler;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import com.manuelr.microservices.cms.commissionservice.dto.ReceiptDto;
import com.manuelr.microservices.cms.commissionservice.entity.Receipt;
import com.manuelr.microservices.cms.commissionservice.web.mapper.ReceiptMapper;
import lombok.AllArgsConstructor;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class ReceiptAssembler implements RepresentationModelAssembler<Receipt, ReceiptDto> {
    private final ReceiptMapper mapper;

    @Override
    public ReceiptDto toModel(Receipt entity) {
        ReceiptDto dto = mapper.receiptToReceiptDto(entity);
        return dto;
    }
}
