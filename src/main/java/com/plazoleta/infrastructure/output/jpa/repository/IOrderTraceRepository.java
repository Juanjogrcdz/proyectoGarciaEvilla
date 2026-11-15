package com.plazoleta.infrastructure.output.jpa.repository;

import com.plazoleta.infrastructure.output.jpa.entity.OrderTraceEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IOrderTraceRepository extends JpaRepository<OrderTraceEntity, Long> {
    // El id desempata si dos cambios cayeran en el mismo instante
    List<OrderTraceEntity> findByOrderIdOrderByChangedAtAscIdAsc(Long orderId);
}