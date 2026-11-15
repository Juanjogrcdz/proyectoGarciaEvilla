package com.plazoleta.infrastructure.input.rest.dto;

import java.time.LocalDateTime;

public class OrderTraceResponseDto {

    private Long orderId;
    private String previousStatus;            // null cuando el pedido se creó
    private String newStatus;
    private LocalDateTime changedAt;
    private Long secondsInPreviousStatus;     // tiempo que estuvo en el estado anterior (null en la primera traza)

    public Long getOrderId() { return orderId; }
    public void setOrderId(Long orderId) { this.orderId = orderId; }
    public String getPreviousStatus() { return previousStatus; }
    public void setPreviousStatus(String previousStatus) { this.previousStatus = previousStatus; }
    public String getNewStatus() { return newStatus; }
    public void setNewStatus(String newStatus) { this.newStatus = newStatus; }
    public LocalDateTime getChangedAt() { return changedAt; }
    public void setChangedAt(LocalDateTime changedAt) { this.changedAt = changedAt; }
    public Long getSecondsInPreviousStatus() { return secondsInPreviousStatus; }
    public void setSecondsInPreviousStatus(Long secondsInPreviousStatus) { this.secondsInPreviousStatus = secondsInPreviousStatus; }
}