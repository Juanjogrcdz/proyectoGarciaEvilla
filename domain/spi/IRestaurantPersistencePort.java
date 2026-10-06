package com.plazoleta.domain.spi;
import com.plazoleta.domain.model.Restaurant;
import java.util.List;

public interface IRestaurantPersistencePort {
    void saveRestaurant(Restaurant restaurant);
    Restaurant findById(Long id); // Necesario para validar el dueño del restaurante`
    List<Restaurant> findAllOrderedByName(int page, int size);
}