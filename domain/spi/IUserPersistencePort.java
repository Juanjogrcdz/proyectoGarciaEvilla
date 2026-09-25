package com.plazoleta.domain.spi;

import com.plazoleta.domain.model.User;


public interface IUserPersistencePort {
    void saveUser(User user);
    boolean existsByEmail(String email);
}