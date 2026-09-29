package com.plazoleta.infrastructure.output.mapper;

import com.plazoleta.domain.model.Dish;
import com.plazoleta.infrastructure.output.entity.DishEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface IDishEntityMapper {

    @Mapping(target = "category.id", source = "categoryId")
    DishEntity toEntity(Dish dish);

    @Mapping(target = "categoryId", source = "category.id")
    Dish toDish(DishEntity dishEntity);
}
