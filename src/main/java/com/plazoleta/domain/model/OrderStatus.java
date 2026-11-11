package com.plazoleta.domain.model;

import java.util.List;

public enum OrderStatus {
    PENDIENTE,
    EN_PREPARACION,
    LISTO,
    ENTREGADO,
    CANCELADO;

    // Estados en los que el pedido sigue "en proceso" (el cliente no puede pedir otro)
    public static final List<OrderStatus> IN_PROGRESS = List.of(PENDIENTE, EN_PREPARACION, LISTO);
}