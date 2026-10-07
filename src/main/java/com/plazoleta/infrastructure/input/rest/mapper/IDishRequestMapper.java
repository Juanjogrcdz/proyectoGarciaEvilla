package com.plazoleta.infrastructure.input.rest.mapper;

import com.plazoleta.domain.model.Dish;
import com.plazoleta.infrastructure.input.rest.dto.DishRequestDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface IDishRequestMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "active", ignore = true)   // lo pone el caso de uso (true por defecto)
    Dish toDish(DishRequestDto dto);
}