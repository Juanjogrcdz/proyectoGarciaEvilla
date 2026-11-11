package com.plazoleta.infrastructure.output.jpa.repository;

import com.plazoleta.infrastructure.output.jpa.entity.DishEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IDishRepository extends JpaRepository<DishEntity, Long> {
    Page<DishEntity> findByRestaurantIdAndActiveTrue(Long restaurantId, Pageable pageable);

    Page<DishEntity> findByRestaurantIdAndActiveTrueAndCategoryIgnoreCase(
            Long restaurantId, String category, Pageable pageable);
}