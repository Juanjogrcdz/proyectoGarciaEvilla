package com.plazoleta.application.usecase;

import com.plazoleta.domain.exception.NotRestaurantOwnerException;
import com.plazoleta.domain.model.Dish;
import com.plazoleta.domain.model.Restaurant;
import com.plazoleta.domain.spi.IDishPersistencePort;
import com.plazoleta.domain.spi.IRestaurantPersistencePort;
import com.plazoleta.domain.spi.IUserContextPort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DishUseCaseStatusTest {

    @Mock private IDishPersistencePort dishPersistencePort;
    @Mock private IRestaurantPersistencePort restaurantPersistencePort;
    @Mock private IUserContextPort userContextPort;
    @InjectMocks private DishUseCase dishUseCase;

    private Dish buildDish(boolean active) {
        Dish dish = new Dish();
        dish.setId(5L);
        dish.setRestaurantId(1L);
        dish.setName("Pizza");
        dish.setActive(active);
        return dish;
    }

    private Restaurant restaurantOwnedBy(Long ownerId) {
        Restaurant restaurant = new Restaurant();
        restaurant.setOwnerId(ownerId);
        return restaurant;
    }

    @Test
    void updateDishStatus_whenOwnerDeactivates_dishBecomesInactive() {
        Dish dish = buildDish(true);
        when(dishPersistencePort.findById(5L)).thenReturn(dish);
        when(restaurantPersistencePort.findById(1L)).thenReturn(restaurantOwnedBy(10L));
        when(userContextPort.getCurrentUserId()).thenReturn(10L);

        dishUseCase.updateDishStatus(5L, false);

        assertFalse(dish.getActive());              // o isActive(), según tu modelo
        verify(dishPersistencePort).saveDish(dish);
    }

    @Test
    void updateDishStatus_whenOwnerActivates_dishBecomesActive() {
        Dish dish = buildDish(false);
        when(dishPersistencePort.findById(5L)).thenReturn(dish);
        when(restaurantPersistencePort.findById(1L)).thenReturn(restaurantOwnedBy(10L));
        when(userContextPort.getCurrentUserId()).thenReturn(10L);

        dishUseCase.updateDishStatus(5L, true);

        assertTrue(dish.getActive());
        verify(dishPersistencePort).saveDish(dish);
    }

    @Test
    void updateDishStatus_whenUserIsNotOwner_throwsAndDoesNotPersist() {
        Dish dish = buildDish(true);
        when(dishPersistencePort.findById(5L)).thenReturn(dish);
        when(restaurantPersistencePort.findById(1L)).thenReturn(restaurantOwnedBy(10L));
        when(userContextPort.getCurrentUserId()).thenReturn(99L);   // dueño de OTRO restaurante

        assertThrows(NotRestaurantOwnerException.class, () -> dishUseCase.updateDishStatus(5L, false));
        assertTrue(dish.getActive());               // el plato quedó intacto
        verify(dishPersistencePort, never()).saveDish(any());
    }

    @Test
    void updateDishStatus_whenDishDoesNotExist_throwsIllegalArgument() {
        when(dishPersistencePort.findById(5L)).thenReturn(null);

        assertThrows(IllegalArgumentException.class, () -> dishUseCase.updateDishStatus(5L, false));
        verify(dishPersistencePort, never()).saveDish(any());
    }

    @Test
    void updateDishStatus_whenStatusIsNull_throwsIllegalArgument() {
        assertThrows(IllegalArgumentException.class, () -> dishUseCase.updateDishStatus(5L, null));
        verify(dishPersistencePort, never()).saveDish(any());
    }
}