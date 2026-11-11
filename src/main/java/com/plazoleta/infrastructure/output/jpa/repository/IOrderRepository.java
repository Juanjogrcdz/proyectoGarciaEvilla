package com.plazoleta.infrastructure.output.jpa.repository;

import com.plazoleta.domain.model.OrderStatus;
import com.plazoleta.infrastructure.output.jpa.entity.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Collection;

public interface IOrderRepository extends JpaRepository<OrderEntity, Long> {
    boolean existsByClientIdAndStatusIn(Long clientId, Collection<OrderStatus> statuses);
        Page<OrderEntity> findByRestaurantIdAndStatus(Long restaurantId, OrderStatus status, Pageable pageable);
}