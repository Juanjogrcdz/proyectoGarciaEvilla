package com.plazoleta.infrastructure.output.jpa.mapper;

import com.plazoleta.domain.model.OrderTrace;
import com.plazoleta.infrastructure.output.jpa.entity.OrderTraceEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface IOrderTraceEntityMapper {

    OrderTraceEntity toEntity(OrderTrace trace);

    @Mapping(target = "secondsInPreviousStatus", ignore = true)   // se calcula en el caso de uso
    OrderTrace toModel(OrderTraceEntity entity);

    List<OrderTrace> toModelList(List<OrderTraceEntity> entities);
}