package com.plazoleta.infrastructure.input.rest.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public class OrderRequestDto {

    @NotNull(message = "El restaurante es obligatorio")
    private Long restaurantId;

    @NotEmpty(message = "El pedido debe incluir al menos un plato")
    @Valid
    private List<OrderItemRequestDto> dishes;

    public Long getRestaurantId() { return restaurantId; }
    public void setRestaurantId(Long restaurantId) { this.restaurantId = restaurantId; }
    public List<OrderItemRequestDto> getDishes() { return dishes; }
    public void setDishes(List<OrderItemRequestDto> dishes) { this.dishes = dishes; }
}