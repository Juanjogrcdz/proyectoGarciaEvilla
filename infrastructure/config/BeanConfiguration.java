package com.plazoleta.infrastructure.config;

import com.plazoleta.application.usecase.UserUseCase;
import com.plazoleta.domain.api.IUserServicePort;
import com.plazoleta.domain.spi.IPasswordEncoderPort;
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
    public IDishServicePort dishServicePort(IDishPersistencePort dishPersistencePort) {
        return new DishUseCase(dishPersistencePort);
    }
}