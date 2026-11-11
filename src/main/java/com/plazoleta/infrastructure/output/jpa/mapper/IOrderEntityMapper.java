package com.plazoleta.infrastructure.output.jpa.mapper;

import com.plazoleta.domain.model.Order;
import com.plazoleta.infrastructure.output.jpa.entity.OrderEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface IOrderEntityMapper {

    OrderEntity toEntity(Order order);

    Order toModel(OrderEntity entity);
}