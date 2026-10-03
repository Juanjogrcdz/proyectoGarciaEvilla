package com.plazoleta.infrastructure.config;

import com.plazoleta.application.usecase.DishUseCase;
import com.plazoleta.application.usecase.RestaurantUseCase;
import com.plazoleta.application.usecase.UserUseCase;
import com.plazoleta.domain.api.IDishServicePort;
import com.plazoleta.domain.api.IRestaurantServicePort;
import com.plazoleta.domain.api.IUserServicePort;
import com.plazoleta.domain.spi.IDishPersistencePort;
import com.plazoleta.domain.spi.IPasswordEncoderPort;
import com.plazoleta.domain.spi.IRestaurantPersistencePort;
import com.plazoleta.domain.spi.IUserContextPort;
import com.plazoleta.domain.spi.IUserPersistencePort;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfiguration {

    @Bean
    public IUserServicePort userServicePort(IUserPersistencePort userPersistencePort,
                                            IPasswordEncoderPort passwordEncoderPort) {
        return new UserUseCase(userPersistencePort, passwordEncoderPort);
    }

    @Bean
    public IRestaurantServicePort restaurantServicePort(IRestaurantPersistencePort restaurantPersistencePort,
                                                        IUserPersistencePort userPersistencePort) {
        return new RestaurantUseCase(restaurantPersistencePort, userPersistencePort);
    }

    @Bean
    public IDishServicePort dishServicePort(IDishPersistencePort dishPersistencePort,
                                            IRestaurantPersistencePort restaurantPersistencePort,
                                            IUserContextPort userContextPort) {
        return new DishUseCase(dishPersistencePort, restaurantPersistencePort, userContextPort);
    }
}