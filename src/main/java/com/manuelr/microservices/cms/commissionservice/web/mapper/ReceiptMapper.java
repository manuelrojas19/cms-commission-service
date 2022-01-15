package com.manuelr.microservices.cms.commissionservice.web.mapper;

import com.manuelr.microservices.cms.commissionservice.dto.ReceiptDto;
import com.manuelr.microservices.cms.commissionservice.entity.Receipt;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

@Mapper(uses = {DataMapper.class})
@Component
public interface ReceiptMapper {
    Receipt receiptDtoToReceipt(ReceiptDto receiptDto);
    ReceiptDto receiptToReceiptDto(Receipt receipt);
}
