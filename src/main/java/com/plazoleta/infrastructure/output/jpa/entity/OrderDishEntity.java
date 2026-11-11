package com.plazoleta.infrastructure.output.jpa.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "order_dishes")
public class OrderDishEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "dish_id", nullable = false)
    private Long dishId;

    @Column(nullable = false)
    private Integer quantity;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getDishId() { return dishId; }
    public void setDishId(Long dishId) { this.dishId = dishId; }
    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }
}