package com.plazoleta.infrastructure.config;

import com.plazoleta.application.usecase.DishUseCase;
import com.plazoleta.application.usecase.OrderTraceUseCase;
import com.plazoleta.application.usecase.OrderUseCase;
import com.plazoleta.application.usecase.RestaurantUseCase;
import com.plazoleta.application.usecase.UserUseCase;
import com.plazoleta.domain.api.IDishServicePort;
import com.plazoleta.domain.api.IOrderServicePort;
import com.plazoleta.domain.api.IOrderTraceServicePort;
import com.plazoleta.domain.api.IRestaurantServicePort;
import com.plazoleta.domain.api.IUserServicePort;
import com.plazoleta.domain.spi.IDishPersistencePort;
import com.plazoleta.domain.spi.INotificationPort;
import com.plazoleta.domain.spi.IOrderPersistencePort;
import com.plazoleta.domain.spi.IOrderTracePersistencePort;
import com.plazoleta.domain.spi.IPasswordEncoderPort;
import com.plazoleta.domain.spi.IPinGeneratorPort;
import com.plazoleta.domain.spi.IRestaurantEmployeePersistencePort;
import com.plazoleta.domain.spi.IRestaurantPersistencePort;
import com.plazoleta.domain.spi.IUserContextPort;
import com.plazoleta.domain.spi.IUserPersistencePort;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfiguration {

    @Bean
    public IUserServicePort userServicePort(IUserPersistencePort userPersistencePort,
                                            IPasswordEncoderPort passwordEncoderPort,
                                            IRestaurantPersistencePort restaurantPersistencePort,
                                            IRestaurantEmployeePersistencePort restaurantEmployeePersistencePort,
                                            IUserContextPort userContextPort) {
        return new UserUseCase(userPersistencePort, passwordEncoderPort, restaurantPersistencePort,
                restaurantEmployeePersistencePort, userContextPort);
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

    @Bean
    public IOrderServicePort orderServicePort(IOrderPersistencePort orderPersistencePort,
                                              IOrderTracePersistencePort orderTracePersistencePort,
                                              IRestaurantPersistencePort restaurantPersistencePort,
                                              IDishPersistencePort dishPersistencePort,
                                              IRestaurantEmployeePersistencePort restaurantEmployeePersistencePort,
                                              IUserPersistencePort userPersistencePort,
                                              IUserContextPort userContextPort,
                                              INotificationPort notificationPort,
                                              IPinGeneratorPort pinGeneratorPort) {
        return new OrderUseCase(orderPersistencePort, orderTracePersistencePort, restaurantPersistencePort,
                dishPersistencePort, restaurantEmployeePersistencePort, userPersistencePort,
                userContextPort, notificationPort, pinGeneratorPort);
    }

    @Bean
    public IOrderTraceServicePort orderTraceServicePort(IOrderPersistencePort orderPersistencePort,
                                                        IOrderTracePersistencePort orderTracePersistencePort,
                                                        IUserContextPort userContextPort) {
        return new OrderTraceUseCase(orderPersistencePort, orderTracePersistencePort, userContextPort);
    }
}