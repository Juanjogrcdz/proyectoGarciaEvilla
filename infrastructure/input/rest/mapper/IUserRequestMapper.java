package com.plazoleta.infrastructure.input.mapper;

import com.plazoleta.domain.model.User;
import com.plazoleta.infrastructure.input.dto.UserRequestDto;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface IUserRequestMapper {
    User toUser(UserRequestDto userRequestDto);
}