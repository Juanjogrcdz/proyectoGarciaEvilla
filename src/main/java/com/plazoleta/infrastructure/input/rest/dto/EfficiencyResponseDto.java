package com.plazoleta.infrastructure.input.rest.dto;

import java.util.List;

public class EfficiencyResponseDto {

    private List<OrderDurationResponseDto> orders;
    private List<EmployeeRankingResponseDto> ranking;

    public List<OrderDurationResponseDto> getOrders() { return orders; }
    public void setOrders(List<OrderDurationResponseDto> orders) { this.orders = orders; }
    public List<EmployeeRankingResponseDto> getRanking() { return ranking; }
    public void setRanking(List<EmployeeRankingResponseDto> ranking) { this.ranking = ranking; }
}