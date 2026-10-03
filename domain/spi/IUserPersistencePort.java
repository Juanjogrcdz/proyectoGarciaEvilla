package com.plazoleta.domain.spi;

import com.plazoleta.domain.model.User;
import java.util.Optional;

public interface IUserPersistencePort {
    void saveUser(User user);
    boolean existsByEmail(String email);

    Optional<User> findByEmail(String email);   
}