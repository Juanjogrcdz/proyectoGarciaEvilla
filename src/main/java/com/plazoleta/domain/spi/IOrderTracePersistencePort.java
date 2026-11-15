package com.plazoleta.domain.spi;

import com.plazoleta.domain.model.OrderTrace;

import java.util.List;
import java.util.Collection;

public interface IOrderTracePersistencePort {
    void saveTrace(OrderTrace trace);

    // Cronológico: del primer cambio al último
    List<OrderTrace> findByOrderIdOrderByChangedAtAsc(Long orderId);

    // Las trazas de varios pedidos de una sola vez
    List<OrderTrace> findByOrderIdIn(Collection<Long> orderIds);
}