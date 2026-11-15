package com.plazoleta.domain.api;

import com.plazoleta.domain.model.OrderTrace;

import java.util.List;

public interface IOrderTraceServicePort {
    List<OrderTrace> getOrderTrace(Long orderId);   // HU17
}