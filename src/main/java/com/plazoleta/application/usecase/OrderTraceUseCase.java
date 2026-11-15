package com.plazoleta.application.usecase;

import com.plazoleta.domain.api.IOrderTraceServicePort;
import com.plazoleta.domain.exception.OrderNotOwnedByClientException;
import com.plazoleta.domain.model.Order;
import com.plazoleta.domain.model.OrderTrace;
import com.plazoleta.domain.spi.IOrderPersistencePort;
import com.plazoleta.domain.spi.IOrderTracePersistencePort;
import com.plazoleta.domain.spi.IUserContextPort;

import java.time.Duration;
import java.util.List;

public class OrderTraceUseCase implements IOrderTraceServicePort {

    private final IOrderPersistencePort orderPersistencePort;
    private final IOrderTracePersistencePort orderTracePersistencePort;
    private final IUserContextPort userContextPort;

    public OrderTraceUseCase(IOrderPersistencePort orderPersistencePort,
                             IOrderTracePersistencePort orderTracePersistencePort,
                             IUserContextPort userContextPort) {
        this.orderPersistencePort = orderPersistencePort;
        this.orderTracePersistencePort = orderTracePersistencePort;
        this.userContextPort = userContextPort;
    }

    // HU17: el cliente ve la trazabilidad SOLO de sus propios pedidos
    @Override
    public List<OrderTrace> getOrderTrace(Long orderId) {
        Long clientId = userContextPort.getCurrentUserId();

        Order order = orderPersistencePort.findById(orderId);
        if (order == null) {
            throw new IllegalArgumentException("El pedido no existe");
        }
        if (!order.getClientId().equals(clientId)) {
            throw new OrderNotOwnedByClientException();
        }

        List<OrderTrace> traces = orderTracePersistencePort.findByOrderIdOrderByChangedAtAsc(orderId);

        // Cuánto tiempo pasó el pedido en el estado anterior = diferencia con la traza previa
        OrderTrace previous = null;
        for (OrderTrace trace : traces) {
            if (previous != null) {
                trace.setSecondsInPreviousStatus(
                        Duration.between(previous.getChangedAt(), trace.getChangedAt()).getSeconds());
            }
            previous = trace;
        }
        return traces;
    }
}