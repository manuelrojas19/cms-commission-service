package com.manuelr.microservices.cms.commissionservice.service;

import com.manuelr.cms.commons.dto.CommissionDto;
import com.manuelr.microservices.cms.commissionservice.dto.ReceiptDto;
import com.manuelr.microservices.cms.commissionservice.entity.Receipt;
import org.springframework.hateoas.CollectionModel;
import org.springframework.stereotype.Service;

public interface ReceiptService {
    CollectionModel<ReceiptDto> findAll(Integer page, Integer size);
    CollectionModel<ReceiptDto> findAllByCommission(String commissionId, Integer page, Integer size);
    ReceiptDto create(ReceiptDto receipt);
}
