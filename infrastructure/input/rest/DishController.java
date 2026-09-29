package com.plazoleta.infrastructure.input.rest;

import com.plazoleta.domain.api.IDishServicePort;
import com.plazoleta.infrastructure.input.rest.dto.DishRequestDto;
import com.plazoleta.infrastructure.input.rest.mapper.IDishRequestMapper;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/dishes")
public class DishController {

    private final IDishServicePort dishServicePort;
    private final IDishRequestMapper dishRequestMapper;

    public DishController(IDishServicePort dishServicePort, IDishRequestMapper dishRequestMapper) {
        this.dishServicePort = dishServicePort;
        this.dishRequestMapper = dishRequestMapper;
    }

    // Regla: Solo el propietario de un restaurante puede crear platos[cite: 4]
    @PreAuthorize("hasRole('PROPIETARIO')")
    @PostMapping
    public ResponseEntity<Void> createDish(@Valid @RequestBody DishRequestDto dishRequestDto) {
        dishServicePort.saveDish(dishRequestMapper.toDish(dishRequestDto));
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}