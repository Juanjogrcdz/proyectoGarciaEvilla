package com.plazoleta.domain.exception;

public class OrderOutsideEmployeeRestaurantException extends RuntimeException {
    public OrderOutsideEmployeeRestaurantException() {
        super("El pedido no pertenece al restaurante donde trabajas");
    }
}