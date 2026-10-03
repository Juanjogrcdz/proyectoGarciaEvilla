package com.plazoleta.domain.spi;
import com.plazoleta.domain.model.Restaurant;

public interface IRestaurantPersistencePort {
    void saveRestaurant(Restaurant restaurant);
    Restaurant findById(Long id); // Necesario para validar el dueño del restaurante
}