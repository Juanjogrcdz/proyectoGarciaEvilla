package com.plazoleta.infrastructure.input.rest;

import com.plazoleta.domain.api.IOrderTraceServicePort;
import com.plazoleta.infrastructure.input.rest.dto.OrderTraceResponseDto;
import com.plazoleta.infrastructure.input.rest.mapper.IOrderTraceResponseMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderTraceController {

    private final IOrderTraceServicePort orderTraceServicePort;
    private final IOrderTraceResponseMapper orderTraceResponseMapper;

    public OrderTraceController(IOrderTraceServicePort orderTraceServicePort,
                                IOrderTraceResponseMapper orderTraceResponseMapper) {
        this.orderTraceServicePort = orderTraceServicePort;
        this.orderTraceResponseMapper = orderTraceResponseMapper;
    }

    // HU17: el cliente consulta el historial de cambios de estado de SU pedido.
    // Ej: GET /orders/1/trace
    @PreAuthorize("hasRole('CLIENTE')")
    @GetMapping("/{id}/trace")
    public ResponseEntity<List<OrderTraceResponseDto>> getOrderTrace(@PathVariable Long id) {
        return ResponseEntity.ok(
                orderTraceResponseMapper.toResponseList(orderTraceServicePort.getOrderTrace(id)));
    }
}