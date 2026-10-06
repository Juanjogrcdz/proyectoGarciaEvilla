package com.plazoleta.infrastructure.input.rest.mapper;

import com.plazoleta.domain.model.Restaurant;
import com.plazoleta.infrastructure.input.rest.dto.RestaurantResponseDto;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface IRestaurantResponseMapper {

    RestaurantResponseDto toResponse(Restaurant restaurant);

    List<RestaurantResponseDto> toResponseList(List<Restaurant> restaurants);
}