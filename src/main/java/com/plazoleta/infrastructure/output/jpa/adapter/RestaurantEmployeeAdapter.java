package com.plazoleta.infrastructure.output.jpa.adapter;

import com.plazoleta.domain.model.RestaurantEmployee;
import com.plazoleta.domain.spi.IRestaurantEmployeePersistencePort;
import com.plazoleta.infrastructure.output.jpa.entity.RestaurantEmployeeEntity;
import com.plazoleta.infrastructure.output.jpa.repository.IRestaurantEmployeeRepository;
import org.springframework.stereotype.Component;

@Component
public class RestaurantEmployeeAdapter implements IRestaurantEmployeePersistencePort {

    private final IRestaurantEmployeeRepository restaurantEmployeeRepository;

    public RestaurantEmployeeAdapter(IRestaurantEmployeeRepository restaurantEmployeeRepository) {
        this.restaurantEmployeeRepository = restaurantEmployeeRepository;
    }

    @Override
    public void saveRestaurantEmployee(RestaurantEmployee restaurantEmployee) {
        RestaurantEmployeeEntity entity = new RestaurantEmployeeEntity();
        entity.setRestaurantId(restaurantEmployee.getRestaurantId());
        entity.setEmployeeId(restaurantEmployee.getEmployeeId());
        restaurantEmployeeRepository.save(entity);
    }

    @Override
    public Long findRestaurantIdByEmployeeId(Long employeeId) {
        return restaurantEmployeeRepository.findByEmployeeId(employeeId)
                .map(RestaurantEmployeeEntity::getRestaurantId)
                .orElse(null);
    }
}