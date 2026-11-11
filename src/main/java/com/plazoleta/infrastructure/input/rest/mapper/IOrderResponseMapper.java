package com.plazoleta.infrastructure.input.rest.mapper;

import com.plazoleta.domain.model.Order;
import com.plazoleta.infrastructure.input.rest.dto.OrderResponseDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface IOrderResponseMapper {

    @Mapping(source = "items", target = "dishes")
    OrderResponseDto toResponse(Order order);

    List<OrderResponseDto> toResponseList(List<Order> orders);
}