package com.plazoleta.infrastructure.input.rest.mapper;

import com.plazoleta.domain.model.Restaurant;
import com.plazoleta.infrastructure.input.rest.dto.RestaurantRequestDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface IRestaurantRequestMapper {

    @Mapping(target = "id", ignore = true)
    Restaurant toRestaurant(RestaurantRequestDto dto);
}