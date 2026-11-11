package com.plazoleta.domain.exception;

public class ClientHasOrderInProgressException extends RuntimeException {
    public ClientHasOrderInProgressException() {
        super("Ya tienes un pedido en proceso; podrás pedir de nuevo cuando sea entregado o cancelado");
    }
}