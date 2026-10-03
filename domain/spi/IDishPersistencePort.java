package com.plazoleta.domain.spi;
import com.plazoleta.domain.model.Dish;

public interface IDishPersistencePort {
    void saveDish(Dish dish);
    Dish findById(Long id);
}