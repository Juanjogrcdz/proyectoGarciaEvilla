package com.plazoleta.infrastructure.input.rest.mapper;

import com.plazoleta.domain.model.Dish;
import com.plazoleta.infrastructure.input.rest.dto.DishResponseDto;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface IDishResponseMapper {

    DishResponseDto toResponse(Dish dish);

    List<DishResponseDto> toResponseList(List<Dish> dishes);
}