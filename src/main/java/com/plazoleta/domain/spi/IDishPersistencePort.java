package com.plazoleta.domain.spi;

import com.plazoleta.domain.model.Dish;
import java.util.List;
public interface IDishPersistencePort {
    void saveDish(Dish dish);     // HU3 (crear) y HU4/HU7 (actualizar: el id ya viene lleno)
    Dish findById(Long id);       // HU4 y HU7
    List<Dish> findActiveByRestaurant(Long restaurantId, String category, int page, int size);
}