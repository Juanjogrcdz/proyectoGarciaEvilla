package com.plazoleta.application.usecase;

import com.plazoleta.domain.api.IOrderServicePort;
import com.plazoleta.domain.exception.ClientHasOrderInProgressException;
import com.plazoleta.domain.exception.EmployeeWithoutRestaurantException;
import com.plazoleta.domain.exception.InvalidOrderStatusException;
import com.plazoleta.domain.exception.OrderOutsideEmployeeRestaurantException;
import com.plazoleta.domain.model.Dish;
import com.plazoleta.domain.model.Order;
import com.plazoleta.domain.model.OrderItem;
import com.plazoleta.domain.model.OrderStatus;
import com.plazoleta.domain.spi.IDishPersistencePort;
import com.plazoleta.domain.spi.IOrderPersistencePort;
import com.plazoleta.domain.spi.IRestaurantEmployeePersistencePort;
import com.plazoleta.domain.spi.IRestaurantPersistencePort;
import com.plazoleta.domain.spi.IUserContextPort;
import com.plazoleta.domain.validation.PaginationValidator;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class OrderUseCase implements IOrderServicePort {

    private final IOrderPersistencePort orderPersistencePort;
    private final IRestaurantPersistencePort restaurantPersistencePort;
    private final IDishPersistencePort dishPersistencePort;
    private final IRestaurantEmployeePersistencePort restaurantEmployeePersistencePort;
    private final IUserContextPort userContextPort;

    public OrderUseCase(IOrderPersistencePort orderPersistencePort,
                        IRestaurantPersistencePort restaurantPersistencePort,
                        IDishPersistencePort dishPersistencePort,
                        IRestaurantEmployeePersistencePort restaurantEmployeePersistencePort,
                        IUserContextPort userContextPort) {
        this.orderPersistencePort = orderPersistencePort;
        this.restaurantPersistencePort = restaurantPersistencePort;
        this.dishPersistencePort = dishPersistencePort;
        this.restaurantEmployeePersistencePort = restaurantEmployeePersistencePort;
        this.userContextPort = userContextPort;
    }

    // HU11
    @Override
    public Order saveOrder(Order order) {
        if (order.getItems() == null || order.getItems().isEmpty()) {
            throw new IllegalArgumentException("El pedido debe incluir al menos un plato");
        }

        if (restaurantPersistencePort.findById(order.getRestaurantId()) == null) {
            throw new IllegalArgumentException("El restaurante no existe");
        }

        validateItems(order);

        // El cliente sale del token, nunca del cuerpo de la petición
        Long clientId = userContextPort.getCurrentUserId();

        if (orderPersistencePort.existsByClientIdAndStatusIn(clientId, OrderStatus.IN_PROGRESS)) {
            throw new ClientHasOrderInProgressException();
        }

        order.setClientId(clientId);
        order.setStatus(OrderStatus.PENDIENTE);
        order.setCreatedAt(LocalDateTime.now());

        return orderPersistencePort.saveOrder(order);
    }

    // HU12: el empleado ve SOLO los pedidos del restaurante donde trabaja
    @Override
    public List<Order> listOrdersByStatus(OrderStatus status, int page, int size) {
        PaginationValidator.validate(page, size);

        if (status == null) {
            throw new IllegalArgumentException("El estado es obligatorio");
        }

        Long restaurantId = getEmployeeRestaurantId(userContextPort.getCurrentUserId());
        return orderPersistencePort.findByRestaurantIdAndStatus(restaurantId, status, page, size);
    }

    // HU13: el empleado se asigna un pedido PENDIENTE de su restaurante y lo pasa a EN_PREPARACION
    @Override
    public Order assignOrder(Long orderId) {
        Long employeeId = userContextPort.getCurrentUserId();
        Long employeeRestaurantId = getEmployeeRestaurantId(employeeId);

        Order order = orderPersistencePort.findById(orderId);
        if (order == null) {
            throw new IllegalArgumentException("El pedido no existe");
        }

        if (!order.getRestaurantId().equals(employeeRestaurantId)) {
            throw new OrderOutsideEmployeeRestaurantException();
        }

        if (order.getStatus() != OrderStatus.PENDIENTE) {
            throw new InvalidOrderStatusException(
                    "Solo se pueden asignar pedidos en estado PENDIENTE; este está en " + order.getStatus());
        }

        // Cambio atómico: solo funciona si nadie se adelantó entre la lectura y la escritura
        boolean updated = orderPersistencePort.updateStatusAndChef(
                orderId, OrderStatus.PENDIENTE, OrderStatus.EN_PREPARACION, employeeId);
        if (!updated) {
            throw new InvalidOrderStatusException("Otro empleado tomó este pedido antes que tú");
        }

        order.setChefId(employeeId);
        order.setStatus(OrderStatus.EN_PREPARACION);
        return order;
    }

    // ----- helpers -----

    private Long getEmployeeRestaurantId(Long employeeId) {
        Long restaurantId = restaurantEmployeePersistencePort.findRestaurantIdByEmployeeId(employeeId);
        if (restaurantId == null) {
            throw new EmployeeWithoutRestaurantException();
        }
        return restaurantId;
    }

    private void validateItems(Order order) {
        Set<Long> seenDishes = new HashSet<>();

        for (OrderItem item : order.getItems()) {
            if (item.getQuantity() == null || item.getQuantity() < 1) {
                throw new IllegalArgumentException("La cantidad de cada plato debe ser mayor a 0");
            }
            if (!seenDishes.add(item.getDishId())) {
                throw new IllegalArgumentException(
                        "El plato " + item.getDishId() + " está repetido; ajusta su cantidad en una sola línea");
            }

            Dish dish = dishPersistencePort.findById(item.getDishId());
            if (dish == null) {
                throw new IllegalArgumentException("El plato " + item.getDishId() + " no existe");
            }
            if (!dish.getRestaurantId().equals(order.getRestaurantId())) {
                throw new IllegalArgumentException(
                        "El plato '" + dish.getName() + "' no pertenece al restaurante del pedido");
            }
            if (!Boolean.TRUE.equals(dish.getActive())) {
                throw new IllegalArgumentException("El plato '" + dish.getName() + "' no está disponible");
            }
        }
    }
}