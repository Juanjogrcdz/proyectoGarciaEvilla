package com.plazoleta.domain.model;

import java.time.LocalDateTime;

public class OrderDuration {
    private final Long orderId;
    private final Long employeeId;
    private final LocalDateTime startedAt;
    private final LocalDateTime finishedAt;
    private final Long durationSeconds;

    public OrderDuration(Long orderId, Long employeeId, LocalDateTime startedAt,
                         LocalDateTime finishedAt, Long durationSeconds) {
        this.orderId = orderId;
        this.employeeId = employeeId;
        this.startedAt = startedAt;
        this.finishedAt = finishedAt;
        this.durationSeconds = durationSeconds;
    }

    public Long getOrderId() { return orderId; }
    public Long getEmployeeId() { return employeeId; }
    public LocalDateTime getStartedAt() { return startedAt; }
    public LocalDateTime getFinishedAt() { return finishedAt; }
    public Long getDurationSeconds() { return durationSeconds; }
}