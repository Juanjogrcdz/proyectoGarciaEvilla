package com.plazoleta.domain.spi;

import com.plazoleta.domain.model.OrderTrace;

import java.util.List;

public interface IOrderTracePersistencePort {
    void saveTrace(OrderTrace trace);

    // Cronológico: del primer cambio al último
    List<OrderTrace> findByOrderIdOrderByChangedAtAsc(Long orderId);
}