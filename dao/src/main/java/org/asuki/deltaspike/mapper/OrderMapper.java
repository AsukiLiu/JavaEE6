package org.asuki.deltaspike.mapper;

import java.util.List;

import javax.inject.Inject;

import org.apache.deltaspike.data.api.mapping.SimpleQueryInOutMapperBase;
import org.asuki.deltaspike.dto.OrderDto;
import org.asuki.deltaspike.dto.TicketDto;
import org.asuki.model.entity.deltaspike.Order;

public class OrderMapper extends SimpleQueryInOutMapperBase<Order, OrderDto> {

    @Inject
    private TicketMapper ticketMapper;

    @Override
    protected Object getPrimaryKey(OrderDto dto) {
        return dto.getId();
    }

    @SuppressWarnings("unchecked")
    @Override
    protected OrderDto toDto(Order entity) {
        OrderDto orderDto = new OrderDto(
                (List<TicketDto>) ticketMapper.mapResultList(entity
                        .getTickets()), entity.getPaymentType(),
                entity.getPaid(), entity.getToPay());

        orderDto.setId(entity.getId());
        return orderDto;
    }

    @Override
    protected Order toEntity(Order entity, OrderDto dto) {
        entity.setPaid(dto.getPaid());
        entity.setTickets(ticketMapper.toEntityList(dto.getTicketsDto()));
        entity.setPaymentType(dto.getPaymentType());
        entity.setToPay(dto.getToPay());
        return entity;
    }
}
