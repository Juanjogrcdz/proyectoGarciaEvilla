package com.plazoleta.domain.api;
import com.plazoleta.domain.model.Dish;

public interface IDishServicePort {
    void saveDish(Dish dish);
}