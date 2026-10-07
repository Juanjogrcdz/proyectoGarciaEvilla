package com.plazoleta.infrastructure.output.jpa.mapper;

import com.plazoleta.domain.model.User;
import com.plazoleta.infrastructure.output.jpa.entity.UserEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface IUserEntityMapper {

    UserEntity toEntity(User user);

    User toModel(UserEntity entity);
}