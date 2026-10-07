package com.plazoleta.domain.spi;

import com.plazoleta.domain.model.Restaurant;

import java.util.List;

public interface IRestaurantPersistencePort {
    void saveRestaurant(Restaurant restaurant);                 // HU2
    Restaurant findById(Long id);                               // HU3, HU4, HU7 (validar dueño)
    List<Restaurant> findAllOrderedByName(int page, int size);  // HU9
}