package com.plazoleta.infrastructure.input.rest.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class OrderItemRequestDto {

    @NotNull(message = "El id del plato es obligatorio")
    private Long dishId;

    @NotNull(message = "La cantidad es obligatoria")
    @Min(value = 1, message = "La cantidad debe ser mayor a 0")
    private Integer quantity;

    public Long getDishId() { return dishId; }
    public void setDishId(Long dishId) { this.dishId = dishId; }
    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }
}