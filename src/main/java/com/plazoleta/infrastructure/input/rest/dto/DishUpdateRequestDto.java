package com.plazoleta.infrastructure.input.rest.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class DishUpdateRequestDto {

    @NotNull(message = "El precio es obligatorio")
    @Positive(message = "El precio debe ser un número entero mayor a 0")
    private Integer price;

    @NotBlank(message = "La descripción es obligatoria")
    private String description;

    public Integer getPrice() { return price; }
    public void setPrice(Integer price) { this.price = price; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}