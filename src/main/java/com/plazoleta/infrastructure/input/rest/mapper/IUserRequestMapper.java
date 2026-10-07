package com.plazoleta.infrastructure.input.rest.mapper;

import com.plazoleta.domain.model.User;
import com.plazoleta.infrastructure.input.rest.dto.ClientRequestDto;
import com.plazoleta.infrastructure.input.rest.dto.EmployeeRequestDto;
import com.plazoleta.infrastructure.input.rest.dto.UserRequestDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface IUserRequestMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "role", ignore = true)       // lo asigna el caso de uso
    User toUser(UserRequestDto dto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "birthDate", ignore = true)
    @Mapping(target = "role", ignore = true)
    User toEmployee(EmployeeRequestDto dto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "birthDate", ignore = true)
    @Mapping(target = "role", ignore = true)
    User toClient(ClientRequestDto dto);
}