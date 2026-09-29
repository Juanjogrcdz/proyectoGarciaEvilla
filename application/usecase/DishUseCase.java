package com.plazoleta.application.usecase;

import com.plazoleta.domain.api.IDishServicePort;
import com.plazoleta.domain.model.Dish;
import com.plazoleta.domain.spi.IDishPersistencePort;

public class DishUseCase implements IDishServicePort {

    private final IDishPersistencePort dishPersistencePort;

    public DishUseCase(IDishPersistencePort dishPersistencePort) {
        this.dishPersistencePort = dishPersistencePort;
    }

    @Override
    public void saveDish(Dish dish) {
        if (dish.getPrice() == null || dish.getPrice() <= 0) {
            throw new IllegalArgumentException("El precio del plato debe ser un número entero positivo y mayor a 0");
        }
        
        // Regla: Por defecto cada plato recién creado tiene la variable activa en true[cite: 4]
        dish.setActive(true);

        dishPersistencePort.saveDish(dish);
    }
}