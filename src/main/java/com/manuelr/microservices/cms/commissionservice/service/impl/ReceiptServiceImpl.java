package com.manuelr.microservices.cms.commissionservice.service.impl;

import com.manuelr.microservices.cms.commissionservice.dto.ReceiptDto;
import com.manuelr.microservices.cms.commissionservice.entity.Commission;
import com.manuelr.microservices.cms.commissionservice.entity.Receipt;
import com.manuelr.microservices.cms.commissionservice.exception.NotFoundException;
import com.manuelr.microservices.cms.commissionservice.repository.CommissionRepository;
import com.manuelr.microservices.cms.commissionservice.repository.ReceiptRepository;
import com.manuelr.microservices.cms.commissionservice.service.ReceiptService;
import com.manuelr.microservices.cms.commissionservice.assembler.ReceiptAssembler;
import com.manuelr.microservices.cms.commissionservice.mapper.ReceiptMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.CollectionModel;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@AllArgsConstructor
public class ReceiptServiceImpl implements ReceiptService {
    private final CommissionRepository commissionRepository;
    private final ReceiptRepository receiptRepository;

    private final PagedResourcesAssembler<Receipt> pagedResourcesAssembler;
    private final ReceiptAssembler receiptAssembler;
    private final ReceiptMapper receiptMapper;

    @Override
    @Transactional(readOnly = true)
    public CollectionModel<ReceiptDto> findAll(Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Receipt> receipts = receiptRepository.findAll(pageable);
        log.info("Retrieve data ---> {}", receipts.getContent());
        return pagedResourcesAssembler.toModel(receipts, receiptAssembler);
    }

    @Override
    public CollectionModel<ReceiptDto> findAllByCommissionId(String commissionId, Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Receipt> receipts = receiptRepository.findAllByCommission_Id(commissionId, pageable);
        log.info("Retrieve data ---> {}", receipts.getContent());
        return pagedResourcesAssembler.toModel(receipts, receiptAssembler);
    }

    @Override
    @Transactional
    public ReceiptDto create(ReceiptDto receipt) {
        Commission commission = commissionRepository.findCommissionById(receipt.getCommissionId())
                .orElseThrow(() -> new NotFoundException("Commission was not found", receipt.getCommissionId()));
        Receipt receiptToSave = receiptMapper.receiptDtoToReceipt(receipt);
        receiptToSave.setCommission(commission);
        return receiptAssembler.toModel(receiptRepository.save(receiptToSave));
    }
}
