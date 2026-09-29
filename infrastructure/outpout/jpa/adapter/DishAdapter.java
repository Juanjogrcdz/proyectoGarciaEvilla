package com.plazoleta.infrastructure.output.adapter;

import com.plazoleta.domain.model.Dish;
import com.plazoleta.domain.spi.IDishPersistencePort;
import com.plazoleta.infrastructure.output.mapper.IDishEntityMapper;
import com.plazoleta.infrastructure.output.repository.IDishRepository;
import org.springframework.stereotype.Component;

@Component
public class DishAdapter implements IDishPersistencePort {

    private final IDishRepository dishRepository;
    private final IDishEntityMapper dishEntityMapper;

    public DishAdapter(IDishRepository dishRepository, IDishEntityMapper dishEntityMapper) {
        this.dishRepository = dishRepository;
        this.dishEntityMapper = dishEntityMapper;
    }

    @Override
    public void saveDish(Dish dish) {
        dishRepository.save(dishEntityMapper.toEntity(dish));
    }
}