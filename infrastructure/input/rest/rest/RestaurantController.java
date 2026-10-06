package com.plazoleta.infrastructure.input.rest;

import com.plazoleta.domain.api.IRestaurantServicePort;
import com.plazoleta.infrastructure.input.rest.dto.RestaurantRequestDto;
import com.plazoleta.infrastructure.input.rest.mapper.IRestaurantRequestMapper;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/restaurants")
public class RestaurantController {

    private final IRestaurantServicePort restaurantServicePort;
    private final IRestaurantRequestMapper restaurantRequestMapper;

    public RestaurantController(IRestaurantServicePort restaurantServicePort,
                                IRestaurantRequestMapper restaurantRequestMapper) {
        this.restaurantServicePort = restaurantServicePort;
        this.restaurantRequestMapper = restaurantRequestMapper;
    }

    // HU2: solo el administrador crea restaurantes
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @PostMapping
    public ResponseEntity<Void> createRestaurant(@Valid @RequestBody RestaurantRequestDto dto) {
        restaurantServicePort.saveRestaurant(restaurantRequestMapper.toRestaurant(dto));
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}