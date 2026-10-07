package com.plazoleta.infrastructure.output.adapter;

import com.plazoleta.domain.model.Restaurant;
import com.plazoleta.domain.spi.IRestaurantPersistencePort;
import com.plazoleta.infrastructure.output.jpa.mapper.IRestaurantEntityMapper;
import com.plazoleta.infrastructure.output.jpa.repository.IRestaurantRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class RestaurantAdapter implements IRestaurantPersistencePort {

    private final IRestaurantRepository restaurantRepository;
    private final IRestaurantEntityMapper restaurantEntityMapper;

    public RestaurantAdapter(IRestaurantRepository restaurantRepository,
                             IRestaurantEntityMapper restaurantEntityMapper) {
        this.restaurantRepository = restaurantRepository;
        this.restaurantEntityMapper = restaurantEntityMapper;
    }

    // HU2
    @Override
    public void saveRestaurant(Restaurant restaurant) {
        restaurantRepository.save(restaurantEntityMapper.toEntity(restaurant));
    }

    // HU3, HU4, HU7
    @Override
    public Restaurant findById(Long id) {
        if (id == null) {
            return null;   // el caso de uso responde "El restaurante no existe"
        }
        return restaurantRepository.findById(id)
                .map(restaurantEntityMapper::toModel)
                .orElse(null);
    }

    // HU9
    @Override
    public List<Restaurant> findAllOrderedByName(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Order.asc("name").ignoreCase()));

        return restaurantRepository.findAll(pageable)
                .getContent()
                .stream()
                .map(restaurantEntityMapper::toModel)
                .toList();
    }
}