package com.manuelr.microservices.cms.commissionservice.web.assembler;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import com.manuelr.microservices.cms.commissionservice.controller.CommissionController;
import com.manuelr.microservices.cms.commissionservice.dto.CommissionDto;
import com.manuelr.microservices.cms.commissionservice.entity.Commission;
import com.manuelr.microservices.cms.commissionservice.web.mapper.CommissionMapper;
import com.manuelr.microservices.cms.commissionservice.web.proxy.EmployeeServiceFeignClient;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class CommissionAssembler implements RepresentationModelAssembler<Commission, CommissionDto> {
    private final CommissionMapper commissionMapper;

    @Override
    public @NonNull CommissionDto toModel(@NonNull Commission entity) {
        CommissionDto commissionDto = commissionMapper.commissionToCommissionDto(entity);
        commissionDto.add(WebMvcLinkBuilder.linkTo(methodOn(CommissionController.class).findCommissionById(entity.getId())).withSelfRel());
        commissionDto.add(linkTo(methodOn(CommissionController.class).findAllCommissions(0, 8)).withRel("commissions"));
        commissionDto.add(linkTo(methodOn(CommissionController.class).deleteCommission(entity.getId())).withRel("delete"));
        commissionDto.add(linkTo(methodOn(EmployeeServiceFeignClient.class).findEmployeeById(entity.getEmployeeId())).withRel("employee"));
        return commissionDto;
    }

}
