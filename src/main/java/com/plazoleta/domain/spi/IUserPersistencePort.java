package com.plazoleta.domain.spi;

import com.plazoleta.domain.model.User;

import java.util.Optional;

public interface IUserPersistencePort {
    void saveUser(User user);                   // HU1, HU6, HU8
    User findById(Long id);                     // HU2 (validar que el dueño exista y sea PROPIETARIO)
    Optional<User> findByEmail(String email);   // HU5 (login)
}