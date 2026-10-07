package com.plazoleta.application.usecase;

import com.plazoleta.domain.model.Restaurant;
import com.plazoleta.domain.model.Role;
import com.plazoleta.domain.model.User;
import com.plazoleta.domain.spi.IRestaurantPersistencePort;
import com.plazoleta.domain.spi.IUserPersistencePort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RestaurantUseCaseTest {

    @Mock private IRestaurantPersistencePort restaurantPersistencePort;
    @Mock private IUserPersistencePort userPersistencePort;
    @InjectMocks private RestaurantUseCase restaurantUseCase;

    private Restaurant buildRestaurant(String name) {
        Restaurant restaurant = new Restaurant();
        restaurant.setName(name);
        restaurant.setOwnerId(10L);
        return restaurant;
    }

    private User userWithRole(String roleName) {
        User user = new User();
        user.setId(10L);
        user.setRole(new Role(1L, roleName, "desc"));
        return user;
    }

    @Test
    void saveRestaurant_withValidOwner_persists() {
        Restaurant restaurant = buildRestaurant("Sabor Casero");
        when(userPersistencePort.findById(10L)).thenReturn(userWithRole("PROPIETARIO"));

        restaurantUseCase.saveRestaurant(restaurant);

        verify(restaurantPersistencePort).saveRestaurant(restaurant);
    }

    @Test
    void saveRestaurant_withNumericName_throwsAndDoesNotPersist() {
        assertThrows(IllegalArgumentException.class,
                () -> restaurantUseCase.saveRestaurant(buildRestaurant("12345")));
        verify(restaurantPersistencePort, never()).saveRestaurant(any());
    }

    @Test
    void saveRestaurant_whenOwnerDoesNotExist_throws() {
        when(userPersistencePort.findById(10L)).thenReturn(null);

        assertThrows(IllegalArgumentException.class,
                () -> restaurantUseCase.saveRestaurant(buildRestaurant("Sabor Casero")));
        verify(restaurantPersistencePort, never()).saveRestaurant(any());
    }

    @Test
    void saveRestaurant_whenUserIsNotOwnerRole_throws() {
        when(userPersistencePort.findById(10L)).thenReturn(userWithRole("CLIENTE"));

        assertThrows(IllegalArgumentException.class,
                () -> restaurantUseCase.saveRestaurant(buildRestaurant("Sabor Casero")));
        verify(restaurantPersistencePort, never()).saveRestaurant(any());
    }
}