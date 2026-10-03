package com.plazoleta.application.usecase;

import com.plazoleta.domain.api.IRestaurantServicePort;
import com.plazoleta.domain.model.Restaurant;
import com.plazoleta.domain.model.User;
import com.plazoleta.domain.spi.IRestaurantPersistencePort;
import com.plazoleta.domain.spi.IUserPersistencePort;

public class RestaurantUseCase implements IRestaurantServicePort {

    private final IRestaurantPersistencePort restaurantPersistencePort;
    private final IUserPersistencePort userPersistencePort;

    public RestaurantUseCase(IRestaurantPersistencePort restaurantPersistencePort,
                             IUserPersistencePort userPersistencePort) {
        this.restaurantPersistencePort = restaurantPersistencePort;
        this.userPersistencePort = userPersistencePort;
    }

    // HU2
    @Override
    public void saveRestaurant(Restaurant restaurant) {
        // El nombre no puede ser solo números
        if (restaurant.getName() != null && restaurant.getName().matches("\\d+")) {
            throw new IllegalArgumentException("El nombre del restaurante no puede ser solo numérico");
        }

        // El propietario debe existir y tener rol PROPIETARIO
        User owner = userPersistencePort.findById(restaurant.getOwnerId());
        if (owner == null) {
            throw new IllegalArgumentException("El propietario no existe");
        }
        if (!isOwnerRole(owner)) {
            throw new IllegalArgumentException("El usuario indicado no tiene el rol de propietario");
        }

        restaurantPersistencePort.saveRestaurant(restaurant);
    }

    // Tolera que en BD el rol se llame "PROPIETARIO" o "ROLE_PROPIETARIO"
    private boolean isOwnerRole(User user) {
        if (user.getRole() == null || user.getRole().getName() == null) {
            return false;
        }
        return user.getRole().getName().replaceFirst("^ROLE_", "").equalsIgnoreCase("PROPIETARIO");
    }
}