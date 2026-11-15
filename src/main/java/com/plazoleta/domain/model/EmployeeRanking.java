package com.plazoleta.domain.model;

public class EmployeeRanking {
    private int position;
    private final Long employeeId;
    private final String employeeName;
    private final int ordersDelivered;
    private final long averageSeconds;

    public EmployeeRanking(int position, Long employeeId, String employeeName,
                           int ordersDelivered, long averageSeconds) {
        this.position = position;
        this.employeeId = employeeId;
        this.employeeName = employeeName;
        this.ordersDelivered = ordersDelivered;
        this.averageSeconds = averageSeconds;
    }

    public int getPosition() { return position; }
    public void setPosition(int position) { this.position = position; }
    public Long getEmployeeId() { return employeeId; }
    public String getEmployeeName() { return employeeName; }
    public int getOrdersDelivered() { return ordersDelivered; }
    public long getAverageSeconds() { return averageSeconds; }
}