package com.plazoleta.infrastructure.input.rest.mapper;

import com.plazoleta.domain.model.Dish;
import com.plazoleta.infrastructure.input.rest.dto.DishRequestDto;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface IDishRequestMapper {
    Dish toDish(DishRequestDto dishRequestDto);
}