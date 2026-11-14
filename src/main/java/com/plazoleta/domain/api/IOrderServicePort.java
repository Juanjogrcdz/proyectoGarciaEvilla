package com.plazoleta.domain.api;

import com.plazoleta.domain.model.Order;
import com.plazoleta.domain.model.OrderStatus;
import java.util.List;

public interface IOrderServicePort {
    Order saveOrder(Order order);   // HU11
    List<Order> listOrdersByStatus(OrderStatus status, int page, int size);   // HU12
    Order assignOrder(Long orderId);   // HU13
}