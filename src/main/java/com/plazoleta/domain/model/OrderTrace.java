package com.plazoleta.domain.model;

import java.time.LocalDateTime;

public class OrderTrace {
    private Long id;
    private Long orderId;
    private Long clientId;
    private OrderStatus previousStatus;          // nulo cuando el pedido se crea
    private OrderStatus newStatus;
    private Long changedBy;                      // id del usuario que hizo el cambio
    private LocalDateTime changedAt;
    private Long secondsInPreviousStatus;        // NO se guarda: se calcula al consultar

    public OrderTrace() {
    }

    public OrderTrace(Long orderId, Long clientId, OrderStatus previousStatus, OrderStatus newStatus,
                      Long changedBy, LocalDateTime changedAt) {
        this.orderId = orderId;
        this.clientId = clientId;
        this.previousStatus = previousStatus;
        this.newStatus = newStatus;
        this.changedBy = changedBy;
        this.changedAt = changedAt;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getOrderId() { return orderId; }
    public void setOrderId(Long orderId) { this.orderId = orderId; }
    public Long getClientId() { return clientId; }
    public void setClientId(Long clientId) { this.clientId = clientId; }
    public OrderStatus getPreviousStatus() { return previousStatus; }
    public void setPreviousStatus(OrderStatus previousStatus) { this.previousStatus = previousStatus; }
    public OrderStatus getNewStatus() { return newStatus; }
    public void setNewStatus(OrderStatus newStatus) { this.newStatus = newStatus; }
    public Long getChangedBy() { return changedBy; }
    public void setChangedBy(Long changedBy) { this.changedBy = changedBy; }
    public LocalDateTime getChangedAt() { return changedAt; }
    public void setChangedAt(LocalDateTime changedAt) { this.changedAt = changedAt; }
    public Long getSecondsInPreviousStatus() { return secondsInPreviousStatus; }
    public void setSecondsInPreviousStatus(Long secondsInPreviousStatus) { this.secondsInPreviousStatus = secondsInPreviousStatus; }
}