package com.plazoleta.domain.spi;

import com.plazoleta.domain.model.Order;
import com.plazoleta.domain.model.OrderStatus;

import java.util.List;

public interface IOrderPersistencePort {
    Order saveOrder(Order order);
    boolean existsByClientIdAndStatusIn(Long clientId, List<OrderStatus> statuses);
}