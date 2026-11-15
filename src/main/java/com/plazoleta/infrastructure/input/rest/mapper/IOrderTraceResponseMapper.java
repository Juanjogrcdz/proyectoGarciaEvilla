package com.plazoleta.infrastructure.input.rest.mapper;

import com.plazoleta.domain.model.OrderTrace;
import com.plazoleta.infrastructure.input.rest.dto.OrderTraceResponseDto;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface IOrderTraceResponseMapper {

    List<OrderTraceResponseDto> toResponseList(List<OrderTrace> traces);
}