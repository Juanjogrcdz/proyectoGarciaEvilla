package com.plazoleta.domain.spi;

import com.plazoleta.domain.model.RestaurantEmployee;

public interface IRestaurantEmployeePersistencePort {
    void saveRestaurantEmployee(RestaurantEmployee restaurantEmployee);

    // Devuelve el id del restaurante donde trabaja el empleado, o null si no tiene
    Long findRestaurantIdByEmployeeId(Long employeeId);
}