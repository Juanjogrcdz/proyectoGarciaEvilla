package com.plazoleta.domain.api;

import com.plazoleta.domain.model.User;

public interface IUserServicePort {
    void saveOwner(User owner);
    void saveEmployee(User employee, Long restaurantId);   // HU6 + HU12
    void saveClient(User client);
}