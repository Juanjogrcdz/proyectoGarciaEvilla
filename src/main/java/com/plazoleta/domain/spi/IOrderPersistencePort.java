package com.plazoleta.domain.spi;

import com.plazoleta.domain.model.Order;
import com.plazoleta.domain.model.OrderStatus;

import java.util.List;

public interface IOrderPersistencePort {
    Order saveOrder(Order order);

    boolean existsByClientIdAndStatusIn(Long clientId, List<OrderStatus> statuses);

    List<Order> findByRestaurantIdAndStatus(Long restaurantId, OrderStatus status, int page, int size);

    Order findById(Long id);

    // Cambia estado y chef SOLO si el pedido sigue en expectedStatus. Devuelve true
    // si lo logró.
    boolean updateStatusAndChef(Long orderId, OrderStatus expectedStatus, OrderStatus newStatus, Long chefId);

    // HU14: pasa a newStatus y guarda el PIN, SOLO si el pedido sigue en
    // expectedStatus
    boolean updateStatusAndPin(Long orderId, OrderStatus expectedStatus, OrderStatus newStatus, String pin);

    // HU15 (y HU16): cambia el estado SOLO si el pedido sigue en expectedStatus
    boolean updateStatus(Long orderId, OrderStatus expectedStatus, OrderStatus newStatus);

        // Todos los pedidos del restaurante en ese estado, sin paginar (para calcular métricas)
    List<Order> findAllByRestaurantIdAndStatus(Long restaurantId, OrderStatus status);
    
}