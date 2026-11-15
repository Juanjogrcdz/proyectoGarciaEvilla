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
import com.plazoleta.infrastructure.input.rest.dto.DeliverOrderRequestDto;

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

    // HU13: el empleado se asigna un pedido pendiente de su restaurante.
    // Ej: PATCH /orders/1/assign
    @PreAuthorize("hasRole('EMPLEADO')")
    @PatchMapping("/{id}/assign")
    public ResponseEntity<OrderResponseDto> assignOrder(@PathVariable Long id) {
        return ResponseEntity.ok(orderResponseMapper.toResponse(orderServicePort.assignOrder(id)));
    }

    // HU14: el empleado marca el pedido como LISTO (se notifica al cliente por SMS
    // con el PIN).
    // Ej: PATCH /orders/1/ready
    @PreAuthorize("hasRole('EMPLEADO')")
    @PatchMapping("/{id}/ready")
    public ResponseEntity<OrderResponseDto> markOrderReady(@PathVariable Long id) {
        return ResponseEntity.ok(orderResponseMapper.toResponse(orderServicePort.markOrderReady(id)));
    }

    // HU15: el empleado entrega el pedido digitando el PIN del cliente.
    // Ej: PATCH /orders/1/deliver body: { "pin": "123456" }
    @PreAuthorize("hasRole('EMPLEADO')")
    @PatchMapping("/{id}/deliver")
    public ResponseEntity<OrderResponseDto> deliverOrder(@PathVariable Long id,
            @Valid @RequestBody DeliverOrderRequestDto dto) {
        return ResponseEntity.ok(orderResponseMapper.toResponse(orderServicePort.deliverOrder(id, dto.getPin())));
    }
}