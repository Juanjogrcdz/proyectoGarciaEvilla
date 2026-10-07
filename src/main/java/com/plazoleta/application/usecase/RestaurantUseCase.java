package com.plazoleta.application.usecase;

import com.plazoleta.domain.api.IRestaurantServicePort;
import com.plazoleta.domain.model.Restaurant;
import com.plazoleta.domain.model.User;
import com.plazoleta.domain.spi.IRestaurantPersistencePort;
import com.plazoleta.domain.spi.IUserPersistencePort;
import java.util.List;

public class RestaurantUseCase implements IRestaurantServicePort {

    private final IRestaurantPersistencePort restaurantPersistencePort;
    private final IUserPersistencePort userPersistencePort;
    private static final int MAX_PAGE_SIZE = 100; 

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
     @Override
    public List<Restaurant> listRestaurants(int page, int size) {
        if (page < 0) {
            throw new IllegalArgumentException("La página no puede ser negativa");
        }
        if (size < 1 || size > MAX_PAGE_SIZE) {
            throw new IllegalArgumentException(
                    "El tamaño de página debe estar entre 1 y " + MAX_PAGE_SIZE);
        }
        return restaurantPersistencePort.findAllOrderedByName(page, size);
    }
}