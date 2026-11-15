package com.plazoleta.infrastructure.input.rest;

import com.plazoleta.domain.api.IOrderEfficiencyServicePort;
import com.plazoleta.infrastructure.input.rest.dto.EfficiencyResponseDto;
import com.plazoleta.infrastructure.input.rest.mapper.IEfficiencyResponseMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/restaurants")
public class OrderEfficiencyController {

    private final IOrderEfficiencyServicePort orderEfficiencyServicePort;
    private final IEfficiencyResponseMapper efficiencyResponseMapper;

    public OrderEfficiencyController(IOrderEfficiencyServicePort orderEfficiencyServicePort,
                                     IEfficiencyResponseMapper efficiencyResponseMapper) {
        this.orderEfficiencyServicePort = orderEfficiencyServicePort;
        this.efficiencyResponseMapper = efficiencyResponseMapper;
    }

    // HU18: el propietario consulta la eficiencia de los pedidos de SU restaurante.
    // Ej: GET /restaurants/1/efficiency
    @PreAuthorize("hasRole('PROPIETARIO')")
    @GetMapping("/{restaurantId}/efficiency")
    public ResponseEntity<EfficiencyResponseDto> getEfficiency(@PathVariable Long restaurantId) {
        return ResponseEntity.ok(
                efficiencyResponseMapper.toResponse(orderEfficiencyServicePort.getEfficiency(restaurantId)));
    }
}