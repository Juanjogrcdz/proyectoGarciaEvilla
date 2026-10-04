package com.plazoleta.application.usecase;

import com.plazoleta.domain.api.IUserServicePort;
import com.plazoleta.domain.model.Role;
import com.plazoleta.domain.model.User;
import com.plazoleta.domain.spi.IPasswordEncoderPort;
import com.plazoleta.domain.spi.IUserPersistencePort;

public class UserUseCase implements IUserServicePort {

    private final IUserPersistencePort userPersistencePort;
    private final IPasswordEncoderPort passwordEncoderPort;

    public UserUseCase(IUserPersistencePort userPersistencePort, IPasswordEncoderPort passwordEncoderPort) {
        this.userPersistencePort = userPersistencePort;
        this.passwordEncoderPort = passwordEncoderPort;
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

    // HU6: el propietario crea cuentas de empleado
    @Override
    public void saveEmployee(User employee) {
        // Cifrar la clave con BCrypt (nunca se guarda en texto plano)
        employee.setPassword(passwordEncoderPort.encode(employee.getPassword()));

        // El rol lo decide el sistema, nunca el cliente
        Role employeeRole = new Role(3L, "ROLE_EMPLEADO", "Empleado de restaurante");
        employee.setRole(employeeRole);

        userPersistencePort.saveUser(employee);
    }
}