package com.plazoleta.infrastructure.input.rest;

import com.plazoleta.domain.api.IOrderServicePort;
import com.plazoleta.domain.model.Order;
import com.plazoleta.domain.model.OrderStatus;
import com.plazoleta.infrastructure.input.rest.dto.OrderRequestDto;
import com.plazoleta.infrastructure.input.rest.dto.OrderResponseDto;
import com.plazoleta.infrastructure.input.rest.mapper.IOrderRequestMapper;
import com.plazoleta.infrastructure.input.rest.mapper.IOrderResponseMapper;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private final IOrderServicePort orderServicePort;
    private final IOrderRequestMapper orderRequestMapper;
    private final IOrderResponseMapper orderResponseMapper;

    public OrderController(IOrderServicePort orderServicePort,
                           IOrderRequestMapper orderRequestMapper,
                           IOrderResponseMapper orderResponseMapper) {
        this.orderServicePort = orderServicePort;
        this.orderRequestMapper = orderRequestMapper;
        this.orderResponseMapper = orderResponseMapper;
    }

    // HU11: solo el cliente realiza pedidos
    @PreAuthorize("hasRole('CLIENTE')")
    @PostMapping
    public ResponseEntity<OrderResponseDto> createOrder(@Valid @RequestBody OrderRequestDto dto) {
        Order created = orderServicePort.saveOrder(orderRequestMapper.toOrder(dto));
        return ResponseEntity.status(HttpStatus.CREATED).body(orderResponseMapper.toResponse(created));
    }

    // HU12: el empleado lista los pedidos de SU restaurante por estado.
    // Ej: GET /orders?status=PENDIENTE&page=0&size=10
    @PreAuthorize("hasRole('EMPLEADO')")
    @GetMapping
    public ResponseEntity<List<OrderResponseDto>> listOrders(
            @RequestParam OrderStatus status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(orderResponseMapper.toResponseList(
                orderServicePort.listOrdersByStatus(status, page, size)));
    }
}