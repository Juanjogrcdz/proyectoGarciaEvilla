package com.plazoleta.infrastructure.output.repository;

import com.plazoleta.infrastructure.output.entity.DishEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IDishRepository extends JpaRepository<DishEntity, Long> {
}