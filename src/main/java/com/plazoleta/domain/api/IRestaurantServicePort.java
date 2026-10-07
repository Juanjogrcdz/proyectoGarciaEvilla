package com.plazoleta.domain.api;

import com.plazoleta.domain.model.Restaurant;
import java.util.List;

public interface IRestaurantServicePort {
    void saveRestaurant(Restaurant restaurant);
    List<Restaurant> listRestaurants(int page, int size);
}