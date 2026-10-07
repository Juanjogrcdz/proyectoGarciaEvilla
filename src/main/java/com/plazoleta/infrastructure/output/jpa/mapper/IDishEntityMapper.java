package com.plazoleta.infrastructure.output.jpa.mapper;

import com.plazoleta.domain.model.Dish;
import com.plazoleta.infrastructure.output.jpa.entity.DishEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface IDishEntityMapper {

    DishEntity toEntity(Dish dish);

    Dish toModel(DishEntity entity);
}