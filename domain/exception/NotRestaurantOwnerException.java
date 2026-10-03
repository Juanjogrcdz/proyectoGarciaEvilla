package com.plazoleta.domain.exception;

public class NotRestaurantOwnerException extends RuntimeException {
    public NotRestaurantOwnerException() {
        super("No tienes permisos: no eres el propietario de este restaurante");
    }
}