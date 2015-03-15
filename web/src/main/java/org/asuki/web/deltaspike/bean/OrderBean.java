package org.asuki.web.deltaspike.bean;

import java.util.List;

import javax.enterprise.inject.Model;
import javax.inject.Inject;

import org.asuki.deltaspike.dto.OrderDto;
import org.asuki.deltaspike.manager.OrderManager;

@Model
public class OrderBean {

    @Inject
    private OrderManager orderManager;

    public List<OrderDto> getAllOrders() {
        return orderManager.getAllOrders();
    }

}
