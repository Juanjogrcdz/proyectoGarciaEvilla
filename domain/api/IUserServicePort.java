package com.plazoleta.domain.api;

import com.plazoleta.domain.model.User;

public interface IUserServicePort {
    void saveOwner(User user);
}