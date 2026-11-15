package com.plazoleta.infrastructure.output.jpa.adapter;

import com.plazoleta.domain.model.OrderTrace;
import com.plazoleta.domain.spi.IOrderTracePersistencePort;
import com.plazoleta.infrastructure.output.jpa.mapper.IOrderTraceEntityMapper;
import com.plazoleta.infrastructure.output.jpa.repository.IOrderTraceRepository;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class OrderTraceAdapter implements IOrderTracePersistencePort {

    private final IOrderTraceRepository orderTraceRepository;
    private final IOrderTraceEntityMapper orderTraceEntityMapper;

    public OrderTraceAdapter(IOrderTraceRepository orderTraceRepository,
                             IOrderTraceEntityMapper orderTraceEntityMapper) {
        this.orderTraceRepository = orderTraceRepository;
        this.orderTraceEntityMapper = orderTraceEntityMapper;
    }

    @Override
    public void saveTrace(OrderTrace trace) {
        orderTraceRepository.save(orderTraceEntityMapper.toEntity(trace));
    }

    @Override
    public List<OrderTrace> findByOrderIdOrderByChangedAtAsc(Long orderId) {
        return orderTraceEntityMapper.toModelList(
                orderTraceRepository.findByOrderIdOrderByChangedAtAscIdAsc(orderId));
    }
}