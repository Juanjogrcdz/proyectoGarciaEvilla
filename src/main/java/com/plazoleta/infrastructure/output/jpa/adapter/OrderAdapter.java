package com.plazoleta.infrastructure.output.jpa.adapter;

import com.plazoleta.domain.model.Order;
import com.plazoleta.domain.model.OrderStatus;
import com.plazoleta.domain.spi.IOrderPersistencePort;
import com.plazoleta.infrastructure.output.jpa.entity.OrderEntity;
import com.plazoleta.infrastructure.output.jpa.mapper.IOrderEntityMapper;
import com.plazoleta.infrastructure.output.jpa.repository.IOrderRepository;
import org.springframework.stereotype.Component;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;

@Component
public class OrderAdapter implements IOrderPersistencePort {

    private final IOrderRepository orderRepository;
    private final IOrderEntityMapper orderEntityMapper;

    public OrderAdapter(IOrderRepository orderRepository, IOrderEntityMapper orderEntityMapper) {
        this.orderRepository = orderRepository;
        this.orderEntityMapper = orderEntityMapper;
    }

    @Override
    public Order saveOrder(Order order) {
        OrderEntity saved = orderRepository.save(orderEntityMapper.toEntity(order));
        return orderEntityMapper.toModel(saved);   // ya trae el id generado
    }

    @Override
    public boolean existsByClientIdAndStatusIn(Long clientId, List<OrderStatus> statuses) {
        return orderRepository.existsByClientIdAndStatusIn(clientId, statuses);
    }
      @Override
    public List<Order> findByRestaurantIdAndStatus(Long restaurantId, OrderStatus status, int page, int size) {
        // Del más antiguo al más reciente: es una cola, el primero en llegar se atiende primero
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Order.asc("createdAt")));

        return orderRepository.findByRestaurantIdAndStatus(restaurantId, status, pageable)
                .getContent()
                .stream()
                .map(orderEntityMapper::toModel)
                .toList();
    }
        @Override
    public Order findById(Long id) {
        if (id == null) {
            return null;
        }
        return orderRepository.findById(id)
                .map(orderEntityMapper::toModel)
                .orElse(null);
    }

    @Override
    public boolean updateStatusAndChef(Long orderId, OrderStatus expectedStatus,
                                       OrderStatus newStatus, Long chefId) {
        return orderRepository.updateStatusAndChef(orderId, expectedStatus, newStatus, chefId) == 1;
    }
}