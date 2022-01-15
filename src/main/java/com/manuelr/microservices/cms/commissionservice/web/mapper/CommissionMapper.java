package com.manuelr.microservices.cms.commissionservice.web.mapper;

import com.manuelr.cms.commons.dto.CommissionDto;
import com.manuelr.microservices.cms.commissionservice.entity.Commission;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

@Mapper(uses = {DataMapper.class})
@Component
public interface CommissionMapper {
    CommissionDto commissionToCommissionDto(Commission commission);
    Commission commissionDtoToCommission(CommissionDto commissionDto);
}
