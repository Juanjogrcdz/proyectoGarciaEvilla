package com.plazoleta.infrastructure.input.rest.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class DishRequestDto {

    @NotBlank(message = "El nombre del plato es obligatorio")
    private String name;

    @NotNull(message = "El precio es obligatorio")
    @Positive(message = "El precio debe ser un número entero mayor a 0")
    private Integer price;

    @NotBlank(message = "La descripción es obligatoria")
    private String description;

    @NotBlank(message = "La URL de la imagen es obligatoria")
    private String imageUrl;

    @NotBlank(message = "La categoría es obligatoria")
    private String category;

    @NotNull(message = "El id del restaurante es obligatorio")
    private Long restaurantId;

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public Integer getPrice() { return price; }
    public void setPrice(Integer price) { this.price = price; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    public Long getRestaurantId() { return restaurantId; }
    public void setRestaurantId(Long restaurantId) { this.restaurantId = restaurantId; }
}