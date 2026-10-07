package com.plazoleta.infrastructure.output.jpa.mapper;

import com.plazoleta.domain.model.Restaurant;
import com.plazoleta.infrastructure.output.jpa.entity.RestaurantEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface IRestaurantEntityMapper {

    RestaurantEntity toEntity(Restaurant restaurant);

    Restaurant toModel(RestaurantEntity entity);
}