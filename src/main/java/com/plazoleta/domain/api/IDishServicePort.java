package com.plazoleta.domain.api;

import com.plazoleta.domain.model.Dish;
import java.util.List;

public interface IDishServicePort {
    void saveDish(Dish dish);
    void updateDish(Long dishId, Integer price, String description);
    void updateDishStatus(Long dishId, Boolean active);   // HU7
    List<Dish> listDishes(Long restaurantId, String category, int page, int size);   // HU10
}   