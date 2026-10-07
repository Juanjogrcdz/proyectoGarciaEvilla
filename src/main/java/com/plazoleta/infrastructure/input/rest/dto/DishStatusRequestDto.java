package com.plazoleta.infrastructure.input.rest.dto;

import jakarta.validation.constraints.NotNull;

public class DishStatusRequestDto {

    @NotNull(message = "El estado del plato es obligatorio")
    private Boolean active;

    public Boolean getActive() { return active; }
    public void setActive(Boolean active) { this.active = active; }
}