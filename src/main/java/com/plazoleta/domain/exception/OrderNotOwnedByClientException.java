package com.plazoleta.domain.exception;

public class OrderNotOwnedByClientException extends RuntimeException {
    public OrderNotOwnedByClientException() {
        super("El pedido no pertenece a este cliente");
    }
}