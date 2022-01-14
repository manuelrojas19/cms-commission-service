package com.manuelr.microservices.cms.commissionservice.service.impl;

import com.manuelr.cms.commons.dto.CommissionDto;
import com.manuelr.cms.commons.dto.EmployeeDto;
import com.manuelr.cms.commons.enums.Role;
import com.manuelr.cms.commons.security.UserData;
import com.manuelr.microservices.cms.commissionservice.exception.BadRequestException;
import com.manuelr.microservices.cms.commissionservice.repository.CommissionRepository;
import com.manuelr.microservices.cms.commissionservice.entity.Commission;
import com.manuelr.microservices.cms.commissionservice.exception.NotFoundException;
import com.manuelr.microservices.cms.commissionservice.service.EmployeeService;
import com.manuelr.microservices.cms.commissionservice.service.CommissionService;
import com.manuelr.microservices.cms.commissionservice.assembler.CommissionAssembler;
import com.manuelr.microservices.cms.commissionservice.mapper.CommissionMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.CollectionModel;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

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
        log.info("Retrieve data ---> {}", commissions.getContent());
        return pagedResourcesAssembler.toModel(commissions, commissionAssembler);
    }

    @Override
    @Transactional(readOnly = true)
    public CollectionModel<CommissionDto> findAllByEmployeeId(Long employeeId, Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Commission> commissions = commissionRepository.findAllByEmployeeId(employeeId, pageable);
        return pagedResourcesAssembler.toModel(commissions, commissionAssembler);
    }

    @Override
    @Transactional(readOnly = true)
    public CollectionModel<CommissionDto> findAllByCurrentEmployeeUser(Integer page, Integer size) {
        Long employeeId = ((UserData) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getPersonId();
        Pageable pageable = PageRequest.of(page, size);
        Page<Commission> commissions = commissionRepository.findAllByEmployeeId(employeeId, pageable);
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
        employeeService.findById(commissionDto.getEmployeeId());
        Commission commissionToSave = commissionMapper.commissionDtoToCommission(commissionDto);
        validateCommission(commissionToSave);
        Commission commission = commissionRepository.save(commissionToSave);
        return commissionAssembler.toModel(commission);
    }

    @Override
    @Transactional
    public CommissionDto update(CommissionDto commissionDto, String id) {
        UserData userData = ((UserData) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        Commission commissionToUpdate = commissionRepository.findCommissionById(id)
                .orElseThrow(() -> new NotFoundException("Commission was not found", id));

        if (!(userData.getRole().equals(Role.EMPLOYEE))) throw new AccessDeniedException("Forbidden");
        if (!(commissionToUpdate.getEmployeeId() == userData.getPersonId()))
            throw new AccessDeniedException("Forbidden");

        commissionToUpdate.setAssignedAmount(commissionDto.getAssignedAmount());
        commissionToUpdate.setPlace(commissionDto.getPlace());
        commissionToUpdate.setType(commissionDto.getType());
        validateCommission(commissionToUpdate);
        return commissionAssembler.toModel(commissionRepository.save(commissionToUpdate));
    }

    @Override
    @Transactional
    public void delete(String id) {
        UserData userData = ((UserData) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        Commission commissionToDelete = commissionRepository.findCommissionById(id)
                .orElseThrow(() -> new NotFoundException("Commission not found", id));

        if (!(userData.getRole().equals(Role.EMPLOYEE))) throw new AccessDeniedException("Forbidden");
        if (!(commissionToDelete.getEmployeeId() == userData.getPersonId()))
            throw new AccessDeniedException("Forbidden");

        commissionRepository.deleteById(id);
    }

    private void validateCommission(Commission commission) {
        LocalDate today = LocalDate.now();
        if (commission.getBeginDate().isBefore(today) || commission.getEndDate().isBefore(today)
                || commission.getEndDate().isBefore(commission.getBeginDate())) {
            throw new BadRequestException("Dates are invalid");
        }
        Long overlappedCommissions = commissionRepository.existsOverlappedCommissions(
                commission.getEmployeeId(),
                commission.getBeginDate(),
                commission.getEndDate());
        if (overlappedCommissions >= 1) {
            throw new BadRequestException("There is a overlap with the dates of another commission");
        }
    }
}
