package com.plazoleta.domain.model;

import java.util.List;

public class EfficiencyReport {
    private final List<OrderDuration> orders;
    private final List<EmployeeRanking> ranking;

    public EfficiencyReport(List<OrderDuration> orders, List<EmployeeRanking> ranking) {
        this.orders = orders;
        this.ranking = ranking;
    }

    public List<OrderDuration> getOrders() { return orders; }
    public List<EmployeeRanking> getRanking() { return ranking; }
}