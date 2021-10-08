package com.manuelr.microservices.cms.commissionservice.service.impl;

import com.manuelr.microservices.cms.commissionservice.dto.CommissionDto;
import com.manuelr.microservices.cms.commissionservice.dto.EmployeeDto;
import com.manuelr.microservices.cms.commissionservice.exception.OverlapDatesException;
import com.manuelr.microservices.cms.commissionservice.repository.CommissionRepository;
import com.manuelr.microservices.cms.commissionservice.entity.Commission;
import com.manuelr.microservices.cms.commissionservice.exception.CommissionNotFoundException;
import com.manuelr.microservices.cms.commissionservice.exception.EmployeeNotFoundException;
import com.manuelr.microservices.cms.commissionservice.service.EmployeeService;
import com.manuelr.microservices.cms.commissionservice.service.CommissionService;
import com.manuelr.microservices.cms.commissionservice.web.assembler.CommissionAssembler;
import com.manuelr.microservices.cms.commissionservice.web.mapper.CommissionMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.CollectionModel;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Objects;

@Service
@Slf4j
@Transactional
@AllArgsConstructor
public class CommissionServiceImpl implements CommissionService {
    private final CommissionRepository commissionRepository;
    private final EmployeeService employeeService;

    private final PagedResourcesAssembler<Commission> pagedResourcesAssembler;
    private final CommissionAssembler commissionAssembler;
    private final CommissionMapper commissionMapper;

    @Override
    public CollectionModel<CommissionDto> findAllCommissions(Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Commission> commissions = commissionRepository.findAll(pageable);
        return pagedResourcesAssembler.toModel(commissions, commissionAssembler);
    }

    @Override
    public CollectionModel<CommissionDto> findCommissionsByEmployeeId(Long employeeId, Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Commission> commissions = commissionRepository.findAllByEmployeeId(employeeId, pageable);
        return pagedResourcesAssembler.toModel(commissions, commissionAssembler);
    }

    @Override
    public CommissionDto findCommissionById(String id) {
        Commission commission = commissionRepository.findCommissionById(id)
                .orElseThrow(() -> new CommissionNotFoundException("Requested resource was not found", id));
        return commissionAssembler.toModel(commission);
    }

    @Override
    public CommissionDto createCommission(CommissionDto commissionDto) {
        EmployeeDto employee = employeeService.findEmployeeById(commissionDto.getEmployeeId());
        if (Objects.isNull(employee))
            throw new EmployeeNotFoundException("Employee was not found");

        LocalDate today = LocalDate.now();

        if (commissionDto.getBeginDate().isBefore(today)
                || commissionDto.getEndDate().isBefore(today)
                || commissionDto.getEndDate().isBefore(commissionDto.getBeginDate())) {
            throw new RuntimeException("Dates are invalid");
        }

        Long overlappedCommissions = commissionRepository.existsOverlappedCommissions(
                commissionDto.getEmployeeId(),
                commissionDto.getBeginDate(),
                commissionDto.getEndDate());

        if (overlappedCommissions >= 1) {
            throw new OverlapDatesException("The dates are overlapped with the dates of another commission");
        }

        Commission commissionEntity = commissionMapper.commissionDtoToCommission(commissionDto);
        Commission commission = commissionRepository.save(commissionEntity);

        return commissionAssembler.toModel(commission);
    }

    @Override
    public void deleteCommission(String id) {
        if (!commissionRepository.existsById(id))
            throw new CommissionNotFoundException("Requested resource was not found", id);
        commissionRepository.deleteById(id);
    }
}
