package com.plazoleta.infrastructure.output.jpa.repository;

import com.plazoleta.domain.model.OrderStatus;
import com.plazoleta.infrastructure.output.jpa.entity.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

import java.util.Collection;

public interface IOrderRepository extends JpaRepository<OrderEntity, Long> {
        boolean existsByClientIdAndStatusIn(Long clientId, Collection<OrderStatus> statuses);

        Page<OrderEntity> findByRestaurantIdAndStatus(Long restaurantId, OrderStatus status, Pageable pageable);

        // Actualización condicional y atómica: solo cambia si el pedido sigue en
        // expectedStatus.
        // Devuelve el número de filas modificadas (1 = éxito, 0 = el estado ya había
        // cambiado).
        @Transactional
        @Modifying
        @Query("UPDATE OrderEntity o SET o.status = :newStatus, o.chefId = :chefId "
                        + "WHERE o.id = :id AND o.status = :expectedStatus")
        int updateStatusAndChef(@Param("id") Long id,
                        @Param("expectedStatus") OrderStatus expectedStatus,
                        @Param("newStatus") OrderStatus newStatus,
                        @Param("chefId") Long chefId);

        @Transactional
        @Modifying
        @Query("UPDATE OrderEntity o SET o.status = :newStatus, o.pin = :pin "
                        + "WHERE o.id = :id AND o.status = :expectedStatus")
        int updateStatusAndPin(@Param("id") Long id,
                        @Param("expectedStatus") OrderStatus expectedStatus,
                        @Param("newStatus") OrderStatus newStatus,
                        @Param("pin") String pin);

        @Transactional
        @Modifying
        @Query("UPDATE OrderEntity o SET o.status = :newStatus "
                        + "WHERE o.id = :id AND o.status = :expectedStatus")
        int updateStatus(@Param("id") Long id,
                        @Param("expectedStatus") OrderStatus expectedStatus,
                        @Param("newStatus") OrderStatus newStatus);

        List<OrderEntity> findAllByRestaurantIdAndStatus(Long restaurantId, OrderStatus status);
}