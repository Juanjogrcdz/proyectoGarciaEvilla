package com.plazoleta.domain.model;

import java.time.LocalDateTime;
import java.util.List;

public class Order {
    private Long id;
    private Long clientId;
    private Long restaurantId;
    private Long chefId;               // nulo hasta que un empleado se asigne el pedido (HU13)
    private OrderStatus status;
    private LocalDateTime createdAt;
    private List<OrderItem> items;
    private String pin;     // PIN de seguridad; se genera al pasar a LISTO. Nunca se expone en la API.


    public Order() {
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getClientId() { return clientId; }
    public void setClientId(Long clientId) { this.clientId = clientId; }
    public Long getRestaurantId() { return restaurantId; }
    public void setRestaurantId(Long restaurantId) { this.restaurantId = restaurantId; }
    public Long getChefId() { return chefId; }
    public void setChefId(Long chefId) { this.chefId = chefId; }
    public OrderStatus getStatus() { return status; }
    public void setStatus(OrderStatus status) { this.status = status; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public List<OrderItem> getItems() { return items; }
    public void setItems(List<OrderItem> items) { this.items = items; }
    public String getPin() { return pin; }
    public void setPin(String pin) { this.pin = pin; }
}