package com.plazoleta.application.usecase;

import com.plazoleta.domain.api.IUserServicePort;
import com.plazoleta.domain.exception.NotRestaurantOwnerException;
import com.plazoleta.domain.model.Restaurant;
import com.plazoleta.domain.model.RestaurantEmployee;
import com.plazoleta.domain.model.Role;
import com.plazoleta.domain.model.User;
import com.plazoleta.domain.spi.IPasswordEncoderPort;
import com.plazoleta.domain.spi.IRestaurantEmployeePersistencePort;
import com.plazoleta.domain.spi.IRestaurantPersistencePort;
import com.plazoleta.domain.spi.IUserContextPort;
import com.plazoleta.domain.spi.IUserPersistencePort;

public class UserUseCase implements IUserServicePort {

    private final IUserPersistencePort userPersistencePort;
    private final IPasswordEncoderPort passwordEncoderPort;
    private final IRestaurantPersistencePort restaurantPersistencePort;
    private final IRestaurantEmployeePersistencePort restaurantEmployeePersistencePort;
    private final IUserContextPort userContextPort;

    public UserUseCase(IUserPersistencePort userPersistencePort,
                       IPasswordEncoderPort passwordEncoderPort,
                       IRestaurantPersistencePort restaurantPersistencePort,
                       IRestaurantEmployeePersistencePort restaurantEmployeePersistencePort,
                       IUserContextPort userContextPort) {
        this.userPersistencePort = userPersistencePort;
        this.passwordEncoderPort = passwordEncoderPort;
        this.restaurantPersistencePort = restaurantPersistencePort;
        this.restaurantEmployeePersistencePort = restaurantEmployeePersistencePort;
        this.userContextPort = userContextPort;
    }

    @Override
    public void saveOwner(User user) {
        // Validar mayoría de edad
        if (!user.isAdult()) {
            throw new IllegalArgumentException("El propietario debe ser mayor de edad.");
        }

        // Cifrar la contraseña con el puerto correspondiente
        user.setPassword(passwordEncoderPort.encode(user.getPassword()));

        // Asignar el Rol de Propietario (ejemplo ID 2 o nombre PROPIETARIO)
        Role ownerRole = new Role(2L, "ROLE_PROPIETARIO", "Propietario de restaurante");
        user.setRole(ownerRole);

        // Guardar a través del puerto de persistencia
        userPersistencePort.saveUser(user);
    }

    // HU6 + HU12: el propietario crea un empleado Y lo asigna a SU restaurante
    @Override
    public void saveEmployee(User employee, Long restaurantId) {
        Restaurant restaurant = restaurantPersistencePort.findById(restaurantId);
        if (restaurant == null) {
            throw new IllegalArgumentException("El restaurante no existe");
        }

        // "Empleados de su empresa": el restaurante debe ser del propietario que hace la petición
        if (!restaurant.getOwnerId().equals(userContextPort.getCurrentUserId())) {
            throw new NotRestaurantOwnerException();
        }

        User savedEmployee = registerWithRole(employee,
                new Role(3L, "ROLE_EMPLEADO", "Empleado de restaurante"));

        restaurantEmployeePersistencePort.saveRestaurantEmployee(
                new RestaurantEmployee(restaurantId, savedEmployee.getId()));
    }

    // HU8: el cliente crea su propia cuenta
    @Override
    public void saveClient(User client) {
        registerWithRole(client, new Role(4L, "ROLE_CLIENTE", "Cliente de la plazoleta"));
    }

    // Receta común: cifrar clave + asignar el rol que decide el SISTEMA + guardar
    private User registerWithRole(User user, Role role) {
        user.setPassword(passwordEncoderPort.encode(user.getPassword()));
        user.setRole(role);
        return userPersistencePort.saveUser(user);
    }
}