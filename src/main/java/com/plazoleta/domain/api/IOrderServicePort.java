package com.plazoleta.domain.api;

import com.plazoleta.domain.model.Order;

public interface IOrderServicePort {
    Order saveOrder(Order order);   // HU11
}