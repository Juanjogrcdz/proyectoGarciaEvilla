package com.plazoleta.application.usecase;

import com.plazoleta.domain.api.IOrderEfficiencyServicePort;
import com.plazoleta.domain.exception.NotRestaurantOwnerException;
import com.plazoleta.domain.model.EfficiencyReport;
import com.plazoleta.domain.model.EmployeeRanking;
import com.plazoleta.domain.model.Order;
import com.plazoleta.domain.model.OrderDuration;
import com.plazoleta.domain.model.OrderStatus;
import com.plazoleta.domain.model.OrderTrace;
import com.plazoleta.domain.model.Restaurant;
import com.plazoleta.domain.model.User;
import com.plazoleta.domain.spi.IOrderPersistencePort;
import com.plazoleta.domain.spi.IOrderTracePersistencePort;
import com.plazoleta.domain.spi.IRestaurantPersistencePort;
import com.plazoleta.domain.spi.IUserContextPort;
import com.plazoleta.domain.spi.IUserPersistencePort;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class OrderEfficiencyUseCase implements IOrderEfficiencyServicePort {

    private final IOrderPersistencePort orderPersistencePort;
    private final IOrderTracePersistencePort orderTracePersistencePort;
    private final IRestaurantPersistencePort restaurantPersistencePort;
    private final IUserPersistencePort userPersistencePort;
    private final IUserContextPort userContextPort;

    public OrderEfficiencyUseCase(IOrderPersistencePort orderPersistencePort,
                                  IOrderTracePersistencePort orderTracePersistencePort,
                                  IRestaurantPersistencePort restaurantPersistencePort,
                                  IUserPersistencePort userPersistencePort,
                                  IUserContextPort userContextPort) {
        this.orderPersistencePort = orderPersistencePort;
        this.orderTracePersistencePort = orderTracePersistencePort;
        this.restaurantPersistencePort = restaurantPersistencePort;
        this.userPersistencePort = userPersistencePort;
        this.userContextPort = userContextPort;
    }

    // HU18: solo el dueño del restaurante ve su eficiencia
    @Override
    public EfficiencyReport getEfficiency(Long restaurantId) {
        Restaurant restaurant = restaurantPersistencePort.findById(restaurantId);
        if (restaurant == null) {
            throw new IllegalArgumentException("El restaurante no existe");
        }
        if (!restaurant.getOwnerId().equals(userContextPort.getCurrentUserId())) {
            throw new NotRestaurantOwnerException();
        }

        // Solo los pedidos ENTREGADOS tienen un "fin" que medir
        List<Order> delivered = orderPersistencePort
                .findAllByRestaurantIdAndStatus(restaurantId, OrderStatus.ENTREGADO);
        if (delivered.isEmpty()) {
            return new EfficiencyReport(List.of(), List.of());
        }

        List<Long> orderIds = delivered.stream().map(Order::getId).toList();
        Map<Long, List<OrderTrace>> tracesByOrder = orderTracePersistencePort.findByOrderIdIn(orderIds)
                .stream()
                .collect(Collectors.groupingBy(OrderTrace::getOrderId));

        List<OrderDuration> durations = new ArrayList<>();
        for (Order order : delivered) {
            List<OrderTrace> traces = tracesByOrder.getOrDefault(order.getId(), List.of());

            // Inicio = traza de creación (sin estado anterior). Fin = traza de entrega.
            LocalDateTime start = traces.stream()
                    .filter(t -> t.getPreviousStatus() == null)
                    .map(OrderTrace::getChangedAt)
                    .min(Comparator.naturalOrder())
                    .orElse(null);
            LocalDateTime end = traces.stream()
                    .filter(t -> t.getNewStatus() == OrderStatus.ENTREGADO)
                    .map(OrderTrace::getChangedAt)
                    .max(Comparator.naturalOrder())
                    .orElse(null);

            if (start == null || end == null) {
                continue;   // pedido anterior a la trazabilidad: no se puede medir
            }
            durations.add(new OrderDuration(order.getId(), order.getChefId(), start, end,
                    Duration.between(start, end).getSeconds()));
        }

        // Los más recientes primero
        durations.sort(Comparator.comparing(OrderDuration::getFinishedAt, Comparator.reverseOrder()));

        return new EfficiencyReport(durations, buildRanking(durations));
    }

    // Tiempo medio por empleado, del más rápido al más lento
    private List<EmployeeRanking> buildRanking(List<OrderDuration> durations) {
        Map<Long, List<OrderDuration>> byEmployee = durations.stream()
                .filter(d -> d.getEmployeeId() != null)
                .collect(Collectors.groupingBy(OrderDuration::getEmployeeId));

        List<EmployeeRanking> ranking = new ArrayList<>();
        for (Map.Entry<Long, List<OrderDuration>> entry : byEmployee.entrySet()) {
            Long employeeId = entry.getKey();
            List<OrderDuration> employeeOrders = entry.getValue();

            double average = employeeOrders.stream()
                    .mapToLong(OrderDuration::getDurationSeconds)
                    .average()
                    .orElse(0);

            User employee = userPersistencePort.findById(employeeId);
            String name = (employee != null)
                    ? employee.getName() + " " + employee.getLastName()
                    : "Empleado " + employeeId;

            ranking.add(new EmployeeRanking(0, employeeId, name, employeeOrders.size(), Math.round(average)));
        }

        ranking.sort(Comparator.comparingLong(EmployeeRanking::getAverageSeconds)
                .thenComparing(EmployeeRanking::getEmployeeId));

        for (int i = 0; i < ranking.size(); i++) {
            ranking.get(i).setPosition(i + 1);
        }
        return ranking;
    }
}