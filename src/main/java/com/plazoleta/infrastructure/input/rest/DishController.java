package com.plazoleta.infrastructure.input.rest;

import com.plazoleta.domain.api.IDishServicePort;
import com.plazoleta.infrastructure.input.rest.dto.DishRequestDto;
import com.plazoleta.infrastructure.input.rest.dto.DishResponseDto;
import com.plazoleta.infrastructure.input.rest.dto.DishStatusRequestDto;
import com.plazoleta.infrastructure.input.rest.dto.DishUpdateRequestDto;
import com.plazoleta.infrastructure.input.rest.mapper.IDishRequestMapper;
import com.plazoleta.infrastructure.input.rest.mapper.IDishResponseMapper;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/dishes")
public class DishController {

    private final IDishServicePort dishServicePort;
    private final IDishRequestMapper dishRequestMapper;
    private final IDishResponseMapper dishResponseMapper;

    public DishController(IDishServicePort dishServicePort,
                          IDishRequestMapper dishRequestMapper,
                          IDishResponseMapper dishResponseMapper) {
        this.dishServicePort = dishServicePort;
        this.dishRequestMapper = dishRequestMapper;
        this.dishResponseMapper = dishResponseMapper;
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

    // HU7
    @PreAuthorize("hasRole('PROPIETARIO')")
    @PatchMapping("/{id}/status")
    public ResponseEntity<Void> updateDishStatus(@PathVariable Long id,
                                                 @Valid @RequestBody DishStatusRequestDto dto) {
        dishServicePort.updateDishStatus(id, dto.getActive());
        return ResponseEntity.ok().build();
    }

    // HU10: el cliente lista el menú de un restaurante.
    // Ej: GET /dishes?restaurantId=1&category=Almuerzos&page=0&size=10
    @PreAuthorize("hasRole('CLIENTE')")
    @GetMapping
    public ResponseEntity<List<DishResponseDto>> listDishes(
            @RequestParam Long restaurantId,
            @RequestParam(required = false) String category,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(dishResponseMapper.toResponseList(
                dishServicePort.listDishes(restaurantId, category, page, size)));
    }
}