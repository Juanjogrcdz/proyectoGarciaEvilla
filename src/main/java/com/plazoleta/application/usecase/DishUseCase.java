package com.plazoleta.application.usecase;

import com.plazoleta.domain.api.IDishServicePort;
import com.plazoleta.domain.exception.NotRestaurantOwnerException;
import com.plazoleta.domain.model.Dish;
import com.plazoleta.domain.model.Restaurant;
import com.plazoleta.domain.spi.IDishPersistencePort;
import com.plazoleta.domain.spi.IRestaurantPersistencePort;
import com.plazoleta.domain.spi.IUserContextPort;

public class DishUseCase implements IDishServicePort {

    private final IDishPersistencePort dishPersistencePort;
    private final IRestaurantPersistencePort restaurantPersistencePort;
    private final IUserContextPort userContextPort;

    public DishUseCase(IDishPersistencePort dishPersistencePort,
                       IRestaurantPersistencePort restaurantPersistencePort,
                       IUserContextPort userContextPort) {
        this.dishPersistencePort = dishPersistencePort;
        this.restaurantPersistencePort = restaurantPersistencePort;
        this.userContextPort = userContextPort;
    }

    // HU3 + HU5
    @Override
    public void saveDish(Dish dish) {
        validatePrice(dish.getPrice());

        Restaurant restaurant = getRestaurantOrThrow(dish.getRestaurantId());
        validateOwnership(restaurant);

        dish.setActive(true);
        dishPersistencePort.saveDish(dish);
    }

    // HU4 + HU5: solo se sobreescriben price y description
    @Override
    public void updateDish(Long dishId, Integer price, String description) {
        validatePrice(price);

        Dish dish = getDishOrThrow(dishId);
        validateOwnership(getRestaurantOrThrow(dish.getRestaurantId()));

        dish.setPrice(price);
        dish.setDescription(description);
        dishPersistencePort.saveDish(dish);
    }

    // HU7: habilitar / deshabilitar (el plato NO se borra, solo cambia su estado)
    @Override
    public void updateDishStatus(Long dishId, Boolean active) {
        if (active == null) {
            throw new IllegalArgumentException("El estado del plato es obligatorio");
        }

        Dish dish = getDishOrThrow(dishId);
        validateOwnership(getRestaurantOrThrow(dish.getRestaurantId()));

        dish.setActive(active);
        dishPersistencePort.saveDish(dish);
    }

    // ----- helpers compartidos por HU3, HU4 y HU7 -----

    private void validatePrice(Integer price) {
        if (price == null || price <= 0) {
            throw new IllegalArgumentException("El precio debe ser mayor a 0");
        }
    }

    private Dish getDishOrThrow(Long dishId) {
        Dish dish = dishPersistencePort.findById(dishId);
        if (dish == null) {
            throw new IllegalArgumentException("El plato no existe");
        }
        return dish;
    }

    private Restaurant getRestaurantOrThrow(Long restaurantId) {
        Restaurant restaurant = restaurantPersistencePort.findById(restaurantId);
        if (restaurant == null) {
            throw new IllegalArgumentException("El restaurante no existe");
        }
        return restaurant;
    }

    private void validateOwnership(Restaurant restaurant) {
        Long currentUserId = userContextPort.getCurrentUserId();
        if (!restaurant.getOwnerId().equals(currentUserId)) {
            throw new NotRestaurantOwnerException();
        }
    }
}