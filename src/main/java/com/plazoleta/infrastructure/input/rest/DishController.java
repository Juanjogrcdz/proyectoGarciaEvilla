package com.plazoleta.infrastructure.input.rest;

import com.plazoleta.domain.api.IDishServicePort;
import com.plazoleta.infrastructure.input.rest.dto.DishRequestDto;
import com.plazoleta.infrastructure.input.rest.dto.DishStatusRequestDto;
import com.plazoleta.infrastructure.input.rest.dto.DishUpdateRequestDto;
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

    // HU3
    @PreAuthorize("hasRole('PROPIETARIO')")
    @PostMapping
    public ResponseEntity<Void> createDish(@Valid @RequestBody DishRequestDto dishRequestDto) {
        dishServicePort.saveDish(dishRequestMapper.toDish(dishRequestDto));
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    // HU4
    @PreAuthorize("hasRole('PROPIETARIO')")
    @PutMapping("/{id}")
    public ResponseEntity<Void> updateDish(@PathVariable Long id,
                                           @Valid @RequestBody DishUpdateRequestDto dto) {
        dishServicePort.updateDish(id, dto.getPrice(), dto.getDescription());
        return ResponseEntity.ok().build();
    }

    // HU7: PATCH porque cambia un solo atributo del plato
    @PreAuthorize("hasRole('PROPIETARIO')")
    @PatchMapping("/{id}/status")
    public ResponseEntity<Void> updateDishStatus(@PathVariable Long id,
                                                 @Valid @RequestBody DishStatusRequestDto dto) {
        dishServicePort.updateDishStatus(id, dto.getActive());
        return ResponseEntity.ok().build();
    }
}