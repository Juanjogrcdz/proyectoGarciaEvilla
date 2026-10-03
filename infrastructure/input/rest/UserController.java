package com.plazoleta.infrastructure.input.rest;

import com.plazoleta.domain.api.IUserServicePort;
import com.plazoleta.infrastructure.input.rest.dto.UserRequestDto;
import com.plazoleta.infrastructure.input.rest.mapper.IUserRequestMapper;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {

    private final IUserServicePort userServicePort;
    private final IUserRequestMapper userRequestMapper;

    public UserController(IUserServicePort userServicePort, IUserRequestMapper userRequestMapper) {
        this.userServicePort = userServicePort;
        this.userRequestMapper = userRequestMapper;
    }

    // HU1: solo el administrador crea propietarios
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @PostMapping("/owner")
    public ResponseEntity<Void> createOwner(@Valid @RequestBody UserRequestDto userRequestDto) {
        userServicePort.saveOwner(userRequestMapper.toUser(userRequestDto));
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}