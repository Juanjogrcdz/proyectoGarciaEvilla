package com.plazoleta.application.usecase;

import com.plazoleta.domain.api.IOrderServicePort;
import com.plazoleta.domain.exception.ClientHasOrderInProgressException;
import com.plazoleta.domain.exception.EmployeeWithoutRestaurantException;
import com.plazoleta.domain.exception.InvalidOrderStatusException;
import com.plazoleta.domain.exception.OrderNotOwnedByClientException;
import com.plazoleta.domain.exception.OrderOutsideEmployeeRestaurantException;
import com.plazoleta.domain.model.Dish;
import com.plazoleta.domain.model.Order;
import com.plazoleta.domain.model.OrderItem;
import com.plazoleta.domain.model.OrderStatus;
import com.plazoleta.domain.model.OrderTrace;
import com.plazoleta.domain.model.Restaurant;
import com.plazoleta.domain.model.User;
import com.plazoleta.domain.spi.IDishPersistencePort;
import com.plazoleta.domain.spi.INotificationPort;
import com.plazoleta.domain.spi.IOrderPersistencePort;
import com.plazoleta.domain.spi.IOrderTracePersistencePort;
import com.plazoleta.domain.spi.IPinGeneratorPort;
import com.plazoleta.domain.spi.IRestaurantEmployeePersistencePort;
import com.plazoleta.domain.spi.IRestaurantPersistencePort;
import com.plazoleta.domain.spi.IUserContextPort;
import com.plazoleta.domain.spi.IUserPersistencePort;
import com.plazoleta.domain.validation.PaginationValidator;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class OrderUseCase implements IOrderServicePort {

    private static final String CANNOT_CANCEL_MESSAGE =
            "Lo sentimos, tu pedido ya está en preparación y no puede cancelarse";

    private final IOrderPersistencePort orderPersistencePort;
    private final IOrderTracePersistencePort orderTracePersistencePort;
    private final IRestaurantPersistencePort restaurantPersistencePort;
    private final IDishPersistencePort dishPersistencePort;
    private final IRestaurantEmployeePersistencePort restaurantEmployeePersistencePort;
    private final IUserPersistencePort userPersistencePort;
    private final IUserContextPort userContextPort;
    private final INotificationPort notificationPort;
    private final IPinGeneratorPort pinGeneratorPort;

    public OrderUseCase(IOrderPersistencePort orderPersistencePort,
                        IOrderTracePersistencePort orderTracePersistencePort,
                        IRestaurantPersistencePort restaurantPersistencePort,
                        IDishPersistencePort dishPersistencePort,
                        IRestaurantEmployeePersistencePort restaurantEmployeePersistencePort,
                        IUserPersistencePort userPersistencePort,
                        IUserContextPort userContextPort,
                        INotificationPort notificationPort,
                        IPinGeneratorPort pinGeneratorPort) {
        this.orderPersistencePort = orderPersistencePort;
        this.orderTracePersistencePort = orderTracePersistencePort;
        this.restaurantPersistencePort = restaurantPersistencePort;
        this.dishPersistencePort = dishPersistencePort;
        this.restaurantEmployeePersistencePort = restaurantEmployeePersistencePort;
        this.userPersistencePort = userPersistencePort;
        this.userContextPort = userContextPort;
        this.notificationPort = notificationPort;
        this.pinGeneratorPort = pinGeneratorPort;
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

        Order saved = orderPersistencePort.saveOrder(order);
        registerTrace(saved, null, OrderStatus.PENDIENTE, clientId);   // HU17: nace el pedido
        return saved;
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

    // HU13: PENDIENTE -> EN_PREPARACION, quedando el empleado como responsable
    @Override
    public Order assignOrder(Long orderId) {
        Long employeeId = userContextPort.getCurrentUserId();
        Order order = getOrderForEmployee(orderId);
        requireStatus(order, OrderStatus.PENDIENTE, "asignar");

        boolean updated = orderPersistencePort.updateStatusAndChef(
                orderId, OrderStatus.PENDIENTE, OrderStatus.EN_PREPARACION, employeeId);
        if (!updated) {
            throw new InvalidOrderStatusException("Otro empleado tomó este pedido antes que tú");
        }

        registerTrace(order, OrderStatus.PENDIENTE, OrderStatus.EN_PREPARACION, employeeId);

        order.setChefId(employeeId);
        order.setStatus(OrderStatus.EN_PREPARACION);
        return order;
    }

    // HU14: EN_PREPARACION -> LISTO, y se avisa al cliente por SMS con el PIN
    @Override
    public Order markOrderReady(Long orderId) {
        Long employeeId = userContextPort.getCurrentUserId();
        Order order = getOrderForEmployee(orderId);
        requireStatus(order, OrderStatus.EN_PREPARACION, "marcar como listos");

        // Se verifica ANTES de cambiar el estado, para no dejar el pedido LISTO sin poder avisar
        User client = userPersistencePort.findById(order.getClientId());
        if (client == null || client.getPhone() == null || client.getPhone().isBlank()) {
            throw new IllegalStateException("No se encontró el teléfono del cliente del pedido");
        }

        String pin = pinGeneratorPort.generatePin();

        boolean updated = orderPersistencePort.updateStatusAndPin(
                orderId, OrderStatus.EN_PREPARACION, OrderStatus.LISTO, pin);
        if (!updated) {
            throw new InvalidOrderStatusException(
                    "El pedido cambió de estado mientras lo procesabas; vuelve a consultarlo");
        }

        registerTrace(order, OrderStatus.EN_PREPARACION, OrderStatus.LISTO, employeeId);

        Restaurant restaurant = restaurantPersistencePort.findById(order.getRestaurantId());
        String restaurantName = (restaurant != null) ? restaurant.getName() : "tu restaurante";
        notificationPort.sendSms(client.getPhone(),
                "Tu pedido #" + orderId + " de " + restaurantName
                        + " está listo. PIN de entrega: " + pin);

        order.setStatus(OrderStatus.LISTO);
        order.setPin(pin);
        return order;
    }

    // HU15: LISTO -> ENTREGADO, solo con el PIN que recibió el cliente
    @Override
    public Order deliverOrder(Long orderId, String pin) {
        Long employeeId = userContextPort.getCurrentUserId();
        Order order = getOrderForEmployee(orderId);
        requireStatus(order, OrderStatus.LISTO, "entregar");

        if (pin == null || !pin.equals(order.getPin())) {
            throw new IllegalArgumentException("El PIN de seguridad es incorrecto");
        }

        boolean updated = orderPersistencePort.updateStatus(orderId, OrderStatus.LISTO, OrderStatus.ENTREGADO);
        if (!updated) {
            throw new InvalidOrderStatusException(
                    "El pedido cambió de estado mientras lo procesabas; vuelve a consultarlo");
        }

        registerTrace(order, OrderStatus.LISTO, OrderStatus.ENTREGADO, employeeId);

        order.setStatus(OrderStatus.ENTREGADO);
        return order;
    }

    // HU16: el cliente cancela SU pedido, solo si sigue PENDIENTE
    @Override
    public Order cancelOrder(Long orderId) {
        Long clientId = userContextPort.getCurrentUserId();

        Order order = orderPersistencePort.findById(orderId);
        if (order == null) {
            throw new IllegalArgumentException("El pedido no existe");
        }
        if (!order.getClientId().equals(clientId)) {
            throw new OrderNotOwnedByClientException();
        }
        if (order.getStatus() != OrderStatus.PENDIENTE) {
            throw new InvalidOrderStatusException(CANNOT_CANCEL_MESSAGE);
        }

        // Atómico: si un empleado lo tomó justo ahora, el cambio no se aplica y el mensaje es el correcto
        boolean updated = orderPersistencePort.updateStatus(
                orderId, OrderStatus.PENDIENTE, OrderStatus.CANCELADO);
        if (!updated) {
            throw new InvalidOrderStatusException(CANNOT_CANCEL_MESSAGE);
        }

        registerTrace(order, OrderStatus.PENDIENTE, OrderStatus.CANCELADO, clientId);

        order.setStatus(OrderStatus.CANCELADO);
        return order;
    }

    // ----- helpers -----

    // HU17: deja constancia de cada cambio de estado
    private void registerTrace(Order order, OrderStatus previous, OrderStatus current, Long changedBy) {
        orderTracePersistencePort.saveTrace(new OrderTrace(
                order.getId(), order.getClientId(), previous, current, changedBy, LocalDateTime.now()));
    }

    // Pedido que existe Y pertenece al restaurante donde trabaja el empleado que hace la petición
    private Order getOrderForEmployee(Long orderId) {
        Long employeeRestaurantId = getEmployeeRestaurantId(userContextPort.getCurrentUserId());

        Order order = orderPersistencePort.findById(orderId);
        if (order == null) {
            throw new IllegalArgumentException("El pedido no existe");
        }
        if (!order.getRestaurantId().equals(employeeRestaurantId)) {
            throw new OrderOutsideEmployeeRestaurantException();
        }
        return order;
    }

    private void requireStatus(Order order, OrderStatus expected, String action) {
        if (order.getStatus() != expected) {
            throw new InvalidOrderStatusException("Solo se pueden " + action
                    + " pedidos en estado " + expected + "; este está en " + order.getStatus());
        }
    }

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