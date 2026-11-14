package com.plazoleta.domain.spi;

import com.plazoleta.domain.model.Order;
import com.plazoleta.domain.model.OrderStatus;

import java.util.List;

public interface IOrderPersistencePort {
    Order saveOrder(Order order);
    boolean existsByClientIdAndStatusIn(Long clientId, List<OrderStatus> statuses);
    List<Order> findByRestaurantIdAndStatus(Long restaurantId, OrderStatus status, int page, int size);
    Order findById(Long id);

    // Cambia estado y chef SOLO si el pedido sigue en expectedStatus. Devuelve true si lo logró.
    boolean updateStatusAndChef(Long orderId, OrderStatus expectedStatus, OrderStatus newStatus, Long chefId);
}