package com.plazoleta.infrastructure.input.rest.dto;

import java.time.LocalDateTime;
import java.util.List;

public class OrderResponseDto {

    private Long id;
    private Long clientId;
    private Long restaurantId;
    private Long chefId;
    private String status;
    private LocalDateTime createdAt;
    private List<OrderItemResponseDto> dishes;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getClientId() { return clientId; }
    public void setClientId(Long clientId) { this.clientId = clientId; }
    public Long getRestaurantId() { return restaurantId; }
    public void setRestaurantId(Long restaurantId) { this.restaurantId = restaurantId; }
    public Long getChefId() { return chefId; }
    public void setChefId(Long chefId) { this.chefId = chefId; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public List<OrderItemResponseDto> getDishes() { return dishes; }
    public void setDishes(List<OrderItemResponseDto> dishes) { this.dishes = dishes; }
}