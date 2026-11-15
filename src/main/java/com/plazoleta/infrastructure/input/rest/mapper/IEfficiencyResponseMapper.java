package com.plazoleta.infrastructure.input.rest.mapper;

import com.plazoleta.domain.model.EfficiencyReport;
import com.plazoleta.infrastructure.input.rest.dto.EfficiencyResponseDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface IEfficiencyResponseMapper {

    EfficiencyResponseDto toResponse(EfficiencyReport report);
}