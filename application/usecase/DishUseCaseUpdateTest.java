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
class DishUseCaseOwnershipTest {

    @Mock private IDishPersistencePort dishPersistencePort;
    @Mock private IRestaurantPersistencePort restaurantPersistencePort;
    @Mock private IUserContextPort userContextPort;
    @InjectMocks private DishUseCase dishUseCase;

    private Dish buildDish() {
        Dish dish = new Dish();
        dish.setRestaurantId(1L);
        dish.setName("Pizza");
        dish.setPrice(15000);
        return dish;
    }

    private Restaurant restaurantOwnedBy(Long ownerId) {
        Restaurant restaurant = new Restaurant();
        restaurant.setOwnerId(ownerId);
        return restaurant;
    }

    // ---------- HU3 + HU5 ----------
    @Test
    void saveDish_whenUserIsOwner_savesActiveDish() {
        Dish dish = buildDish();
        when(restaurantPersistencePort.findById(1L)).thenReturn(restaurantOwnedBy(10L));
        when(userContextPort.getCurrentUserId()).thenReturn(10L);

        dishUseCase.saveDish(dish);

        assertTrue(dish.getActive());               // o isActive(), según tu modelo
        verify(dishPersistencePort).saveDish(dish);
    }

    @Test
    void saveDish_whenUserIsNotOwner_throwsAndDoesNotPersist() {
        when(restaurantPersistencePort.findById(1L)).thenReturn(restaurantOwnedBy(10L));
        when(userContextPort.getCurrentUserId()).thenReturn(99L);

        assertThrows(NotRestaurantOwnerException.class, () -> dishUseCase.saveDish(buildDish()));
        verify(dishPersistencePort, never()).saveDish(any());
    }

    @Test
    void saveDish_whenRestaurantDoesNotExist_throwsIllegalArgument() {
        when(restaurantPersistencePort.findById(1L)).thenReturn(null);

        assertThrows(IllegalArgumentException.class, () -> dishUseCase.saveDish(buildDish()));
        verify(dishPersistencePort, never()).saveDish(any());
    }

    @Test
    void saveDish_whenPriceIsInvalid_throwsIllegalArgument() {
        Dish dish = buildDish();
        dish.setPrice(0);

        assertThrows(IllegalArgumentException.class, () -> dishUseCase.saveDish(dish));
        verify(dishPersistencePort, never()).saveDish(any());
    }

    // ---------- HU4 + HU5 ----------
    @Test
    void updateDish_whenUserIsOwner_updatesOnlyPriceAndDescription() {
        Dish existing = buildDish();
        existing.setId(5L);
        when(dishPersistencePort.findById(5L)).thenReturn(existing);
        when(restaurantPersistencePort.findById(1L)).thenReturn(restaurantOwnedBy(10L));
        when(userContextPort.getCurrentUserId()).thenReturn(10L);

        dishUseCase.updateDish(5L, 20000, "Nueva descripción");

        assertEquals(20000, existing.getPrice());
        assertEquals("Nueva descripción", existing.getDescription());
        assertEquals("Pizza", existing.getName());  // el nombre NO cambia
        verify(dishPersistencePort).saveDish(existing);
    }

    @Test
    void updateDish_whenUserIsNotOwner_throwsAndDoesNotPersist() {
        Dish existing = buildDish();
        existing.setId(5L);
        when(dishPersistencePort.findById(5L)).thenReturn(existing);
        when(restaurantPersistencePort.findById(1L)).thenReturn(restaurantOwnedBy(10L));
        when(userContextPort.getCurrentUserId()).thenReturn(99L);

        assertThrows(NotRestaurantOwnerException.class, () -> dishUseCase.updateDish(5L, 20000, "x"));
        verify(dishPersistencePort, never()).saveDish(any());
    }

    @Test
    void updateDish_whenDishDoesNotExist_throwsIllegalArgument() {
        when(dishPersistencePort.findById(5L)).thenReturn(null);

        assertThrows(IllegalArgumentException.class, () -> dishUseCase.updateDish(5L, 20000, "x"));
        verify(dishPersistencePort, never()).saveDish(any());
    }
}