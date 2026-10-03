package com.plazoleta.domain.api;

import com.plazoleta.domain.model.Restaurant;

public interface IRestaurantServicePort {
    void saveRestaurant(Restaurant restaurant);
}