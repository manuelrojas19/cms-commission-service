package com.manuelr.microservices.cms.commissionservice.service.impl;

import com.manuelr.cms.commons.dto.CommissionDto;
import com.manuelr.cms.commons.security.UserData;
import com.manuelr.microservices.cms.commissionservice.entity.Employee;
import com.manuelr.microservices.cms.commissionservice.enums.ApprovalStatus;
import com.manuelr.microservices.cms.commissionservice.exception.BadRequestException;
import com.manuelr.microservices.cms.commissionservice.repository.CommissionRepository;
import com.manuelr.microservices.cms.commissionservice.entity.Commission;
import com.manuelr.microservices.cms.commissionservice.exception.NotFoundException;
import com.manuelr.microservices.cms.commissionservice.service.CommissionService;
import com.manuelr.microservices.cms.commissionservice.service.EmployeeService;
import com.manuelr.microservices.cms.commissionservice.web.assembler.CommissionAssembler;
import com.manuelr.microservices.cms.commissionservice.web.mapper.CommissionMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.CollectionModel;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.function.Predicate;

@Slf4j
@Service
@AllArgsConstructor
public class CommissionServiceImpl implements CommissionService {
    private final CommissionRepository commissionRepository;
    private final EmployeeService employeeService;

    private final PagedResourcesAssembler<Commission> pagedResourcesAssembler;
    private final CommissionAssembler commissionAssembler;
    private final CommissionMapper commissionMapper;

    @Override
    @Transactional(readOnly = true)
    public CollectionModel<CommissionDto> findAll(Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Commission> commissions = commissionRepository.findAll(pageable);
        if (commissions.isEmpty()) throw new NotFoundException("Commissions not found", "0");
        log.info("Retrieve data ---> {}", commissions.getContent());
        return pagedResourcesAssembler.toModel(commissions, commissionAssembler);
    }

    @Override
    @Transactional(readOnly = true)
    public CollectionModel<CommissionDto> findAllByEmployee(Long employeePersonId, Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page, size);
        Employee employee = employeeService.findByPersonId(employeePersonId);
        Page<Commission> commissions = commissionRepository.findAllByEmployee(employee, pageable);
        if (commissions.isEmpty()) throw new NotFoundException("Commissions not found", "0");
        return pagedResourcesAssembler.toModel(commissions, commissionAssembler);
    }

    @Override
    @Transactional(readOnly = true)
    public CollectionModel<CommissionDto> findAllByCurrentEmployeeUser(Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page, size);
        Employee employee = employeeService.findCurrentEmployeeUser();
        Page<Commission> commissions = commissionRepository.findAllByEmployee(employee, pageable);
        if (commissions.isEmpty()) throw new NotFoundException("Commissions not found", "0");
        return pagedResourcesAssembler.toModel(commissions, commissionAssembler);
    }

    @Override
    @Transactional(readOnly = true)
    public CommissionDto findById(String id) {
        Commission commission = commissionRepository.findCommissionById(id)
                .orElseThrow(() -> new NotFoundException("Requested resource was not found", id));
        return commissionAssembler.toModel(commission);
    }

    @Override
    @Transactional
    public CommissionDto create(CommissionDto commissionDto) {
        Long employeePersonId = ((UserData) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getPersonId();
        Employee employee = employeeService.findByPersonId(employeePersonId);
        Commission commission = commissionMapper.commissionDtoToCommission(commissionDto);
        validateCommission(commission, employee);
        commission.setEmployee(employee);
        return commissionAssembler.toModel(commissionRepository.save(commission));
    }

    @Override
    @Transactional
    public void delete(String id) {
        Commission commission = commissionRepository.findCommissionById(id)
                .orElseThrow(() -> new NotFoundException("Commission was not found", id));
        commissionRepository.delete(commission);
    }

    @Override
    @Transactional
    public void approveByManager(String id) {
        Commission commission = commissionRepository.findCommissionById(id)
                .orElseThrow(() -> new NotFoundException("Commission was not found", id));
        if (commission.getManagerApprovalStatus().equals(ApprovalStatus.REJECTED))
            throw new BadRequestException("The commission has ben rejected");
        if (commission.getManagerApprovalStatus().equals(ApprovalStatus.AUTHORIZED))
            throw new BadRequestException("The commission is already approved");
        commission.setManagerApprovalStatus(ApprovalStatus.AUTHORIZED);
        commissionRepository.save(commission);
    }

    private void validateCommission(Commission commission, Employee employee) {
        if (!validateDates(commission.getBeginDate(), commission.getEndDate()))
            throw new BadRequestException("Dates are invalid");
        List<Commission> commissions = commissionRepository.findAllByEmployee(employee);
        log.info("Commissions by the user --> {}", commissions);
        if (commissions.stream().anyMatch(betweenCommissionDatesPredicate(commission.getBeginDate(),
                commission.getEndDate())))
            throw new BadRequestException("There is a overlap with the dates of another commission");
    }

    private boolean validateDates(LocalDate begin, LocalDate end) {
        LocalDate today = LocalDate.now();
        return !(begin.isBefore(today) || end.isBefore(today) || end.isBefore(begin));
    }

    private Predicate<Commission> betweenCommissionDatesPredicate(LocalDate beginDate, LocalDate endDate) {
        return c -> dateIsBetween(beginDate, c.getBeginDate(), c.getEndDate()) ||
                dateIsBetween(endDate, c.getBeginDate(), c.getEndDate()) ||
                dateIsBetween(c.getBeginDate(), beginDate, endDate) ||
                dateIsBetween(c.getEndDate(), beginDate, endDate);
    }

    private boolean dateIsBetween(LocalDate date, LocalDate begin, LocalDate end) {
        return (date.isAfter(begin) || date.isEqual(begin))
                && (date.isBefore(end) || date.isEqual(end));
    }
}
