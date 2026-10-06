package com.plazoleta.infrastructure.input.mapper;

import com.plazoleta.domain.model.User;
import com.plazoleta.infrastructure.input.dto.UserRequestDto;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import com.plazoleta.infrastructure.input.rest.dto.EmployeeRequestDto;
import org.mapstruct.Mapping;
import com.plazoleta.infrastructure.input.rest.dto.ClientRequestDto;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface IUserRequestMapper {
  User toUser(UserRequestDto userRequestDto);

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "birthDate", ignore = true)
  @Mapping(target = "role", ignore = true) // lo asigna el caso de uso
  User toEmployee(EmployeeRequestDto dto);

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "birthDate", ignore = true)
  @Mapping(target = "role", ignore = true) // lo asigna el caso de uso
  User toClient(ClientRequestDto dto);
}