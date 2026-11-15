package com.plazoleta.infrastructure.input.rest.mapper;

import com.plazoleta.domain.model.Order;
import com.plazoleta.infrastructure.input.rest.dto.OrderRequestDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface IOrderRequestMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "clientId", ignore = true)    // sale del token
    @Mapping(target = "chefId", ignore = true)
    @Mapping(target = "status", ignore = true)      // lo fija el caso de uso (PENDIENTE)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(source = "dishes", target = "items")
    @Mapping(target = "pin", ignore = true)
    Order toOrder(OrderRequestDto dto);
}