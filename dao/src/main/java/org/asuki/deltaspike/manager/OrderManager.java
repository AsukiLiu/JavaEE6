package org.asuki.deltaspike.manager;

import java.util.List;

import javax.inject.Inject;

import org.asuki.deltaspike.dto.OrderDto;
import org.asuki.deltaspike.repository.OrderRepository;

public class OrderManager {

    @Inject
    private OrderRepository orderRepository;

    public List<OrderDto> getAllOrders() {
        return orderRepository.findAll();
    }

    public OrderDto save(OrderDto orderDto) {
        return orderRepository.save(orderDto);
    }
}
