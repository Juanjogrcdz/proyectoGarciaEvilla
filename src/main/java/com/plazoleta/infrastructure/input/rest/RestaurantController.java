package com.plazoleta.infrastructure.input.rest;

import com.plazoleta.domain.api.IRestaurantServicePort;
import com.plazoleta.infrastructure.input.rest.dto.RestaurantRequestDto;
import com.plazoleta.infrastructure.input.rest.dto.RestaurantResponseDto;
import com.plazoleta.infrastructure.input.rest.mapper.IRestaurantRequestMapper;
import com.plazoleta.infrastructure.input.rest.mapper.IRestaurantResponseMapper;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/restaurants")
public class RestaurantController {

    private final IRestaurantServicePort restaurantServicePort;
    private final IRestaurantRequestMapper restaurantRequestMapper;
    private final IRestaurantResponseMapper restaurantResponseMapper;

    public RestaurantController(IRestaurantServicePort restaurantServicePort,
                                IRestaurantRequestMapper restaurantRequestMapper,
                                IRestaurantResponseMapper restaurantResponseMapper) {
        this.restaurantServicePort = restaurantServicePort;
        this.restaurantRequestMapper = restaurantRequestMapper;
        this.restaurantResponseMapper = restaurantResponseMapper;
    }

    // HU2: solo el administrador crea restaurantes
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @PostMapping
    public ResponseEntity<Void> createRestaurant(@Valid @RequestBody RestaurantRequestDto dto) {
        restaurantServicePort.saveRestaurant(restaurantRequestMapper.toRestaurant(dto));
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    // HU9: el cliente lista restaurantes (A-Z), paginados.  Ej: GET /restaurants?page=0&size=10
    @PreAuthorize("hasRole('CLIENTE')")
    @GetMapping
    public ResponseEntity<List<RestaurantResponseDto>> listRestaurants(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(
                restaurantResponseMapper.toResponseList(restaurantServicePort.listRestaurants(page, size)));
    }
}