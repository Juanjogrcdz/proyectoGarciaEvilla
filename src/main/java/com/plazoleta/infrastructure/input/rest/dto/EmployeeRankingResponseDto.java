package com.plazoleta.infrastructure.input.rest.dto;

public class EmployeeRankingResponseDto {

    private int position;
    private Long employeeId;
    private String employeeName;
    private int ordersDelivered;
    private long averageSeconds;

    public int getPosition() { return position; }
    public void setPosition(int position) { this.position = position; }
    public Long getEmployeeId() { return employeeId; }
    public void setEmployeeId(Long employeeId) { this.employeeId = employeeId; }
    public String getEmployeeName() { return employeeName; }
    public void setEmployeeName(String employeeName) { this.employeeName = employeeName; }
    public int getOrdersDelivered() { return ordersDelivered; }
    public void setOrdersDelivered(int ordersDelivered) { this.ordersDelivered = ordersDelivered; }
    public long getAverageSeconds() { return averageSeconds; }
    public void setAverageSeconds(long averageSeconds) { this.averageSeconds = averageSeconds; }
}