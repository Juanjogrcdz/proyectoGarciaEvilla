package com.plazoleta.infrastructure.output.adapter;

import com.plazoleta.domain.model.Dish;
import com.plazoleta.domain.spi.IDishPersistencePort;
import com.plazoleta.infrastructure.output.jpa.mapper.IDishEntityMapper;
import com.plazoleta.infrastructure.output.jpa.repository.IDishRepository;
import org.springframework.stereotype.Component;

@Component
public class DishAdapter implements IDishPersistencePort {

    private final IDishRepository dishRepository;
    private final IDishEntityMapper dishEntityMapper;

    public DishAdapter(IDishRepository dishRepository, IDishEntityMapper dishEntityMapper) {
        this.dishRepository = dishRepository;
        this.dishEntityMapper = dishEntityMapper;
    }

    // Si el plato trae id, JPA hace UPDATE; si no, INSERT
    @Override
    public void saveDish(Dish dish) {
        dishRepository.save(dishEntityMapper.toEntity(dish));
    }

    @Override
    public Dish findById(Long id) {
        if (id == null) {
            return null;
        }
        return dishRepository.findById(id)
                .map(dishEntityMapper::toModel)
                .orElse(null);
    }
}