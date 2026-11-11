package com.plazoleta.infrastructure.output.adapter;

import com.plazoleta.domain.model.Dish;
import com.plazoleta.domain.spi.IDishPersistencePort;
import com.plazoleta.infrastructure.output.jpa.mapper.IDishEntityMapper;
import com.plazoleta.infrastructure.output.jpa.repository.IDishRepository;
import org.springframework.stereotype.Component;
import com.plazoleta.infrastructure.output.jpa.entity.DishEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import java.util.List;

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
    @Override
    public List<Dish> findActiveByRestaurant(Long restaurantId, String category, int page, int size) {
        // Menú agrupado por categoría y, dentro de cada una, por nombre
        Pageable pageable = PageRequest.of(page, size, Sort.by(
                Sort.Order.asc("category").ignoreCase(),
                Sort.Order.asc("name").ignoreCase()));

        Page<DishEntity> result = (category == null)
                ? dishRepository.findByRestaurantIdAndActiveTrue(restaurantId, pageable)
                : dishRepository.findByRestaurantIdAndActiveTrueAndCategoryIgnoreCase(
                        restaurantId, category, pageable);

        return result.getContent()
                .stream()
                .map(dishEntityMapper::toModel)
                .toList();
    }
}