package org.asuki.web.deltaspike.bean;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;
import javax.enterprise.inject.Model;
import javax.inject.Inject;

import org.apache.deltaspike.core.api.config.view.ViewConfig;
import org.apache.deltaspike.core.api.config.view.navigation.NavigationParameterContext;
import org.asuki.deltaspike.dto.BusDto;
import org.asuki.deltaspike.dto.LineDto;
import org.asuki.deltaspike.dto.OrderDto;
import org.asuki.deltaspike.dto.SeatsDto;
import org.asuki.deltaspike.dto.TicketDto;
import org.asuki.deltaspike.manager.OrderManager;
import org.asuki.deltaspike.mapper.BusMapper;
import org.asuki.model.entity.deltaspike.Bus;
import org.asuki.web.deltaspike.Pages;
import org.asuki.web.deltaspike.Pages.Access;
import org.asuki.web.deltaspike.Utils;
import org.asuki.web.deltaspike.annotation.Produced;

@Model
public class NavigationBean {

    private static final int FIRST_CLASS = 3;

    @Inject
    @Produced
    private TicketDto ticketDto;

    @Inject
    @Produced
    private LineDto lineDto;

    @Inject
    @Produced
    private SeatsDto seatsDto;

    @Inject
    @Produced
    private OrderDto orderDto;

    @Inject
    private OrderManager orderManager;

    @Inject
    private BusMapper busMapper;

    @Inject
    private GroupBean groupBean;

    @Inject
    private Utils utils;

    @Inject
    private NavigationParameterContext navigationParameterContext;

    public Class<? extends ViewConfig> submitBusLine() {
        if (lineDto == null || lineDto.getDeparture() == null
                || lineDto.getArrival() == null
                || lineDto.getDeparture().equals(lineDto.getArrival())) {
            return null;
        }

        return Access.Date.class;
    }

    public Class<? extends ViewConfig> submitTicket(Bus bus) {
        ticketDto.setBusDto((BusDto) busMapper.mapResult(bus));
        return utils.isGroup() ? null : Access.Seat.class;
    }

    public Class<? extends ViewConfig> submitSeats() {
        if (ticketDto.getBusDto() == null
                || ticketDto.getBusDto().getLineDto() == null
                || ticketDto.getBusDto().getDate() == null) {
            return Access.BusLine.class;
        }

        if (seatsDto.getChosenSeats() == null
                || seatsDto.getChosenSeats().size() == 0) {
            return Access.Seat.class;
        }

        List<TicketDto> tickets = new ArrayList<>();
        orderDto.setToPay(addTickets(tickets, seatsDto.getChosenSeats()));
        orderDto.setTicketsDto(tickets);

        return utils.isGroup() ? null : Access.Overview.class;
    }

    private double addTickets(List<TicketDto> tickets,
            @Nonnull List<String> seatsToAdd) {
        double price = 0;

        for (String seat : seatsToAdd) {
            boolean isFirstClass = Integer.valueOf(seat.substring(0,
                    seat.length() - 1)) <= FIRST_CLASS;

            TicketDto orderTicket = new TicketDto(ticketDto.getBusDto(), seat,
                    isFirstClass);
            tickets.add(orderTicket);

            price += ticketDto.getBusDto().getLineDto().getPrice()
                    * (isFirstClass ? 1.5 : 1.0);
        }

        return price;
    }

    public Class<? extends ViewConfig> submitOrder() {
        OrderDto savedOrder = orderManager.save(orderDto);

        navigationParameterContext.addPageParameter("orderId",
                savedOrder.getId());
        navigationParameterContext.addPageParameter("price",
                savedOrder.getToPay());

        groupBean.resetBusLine();

        return Pages.Ordered.class;
    }

    public Class<? extends ViewConfig> backToBusLine() {
        return Access.BusLine.class;
    }

    public Class<? extends ViewConfig> backToDate() {
        return Access.Date.class;
    }

    public Class<? extends ViewConfig> backToSeat() {
        return Access.Seat.class;
    }

    public Class<? extends ViewConfig> toSinglePage() {
        return Pages.Group.SinglePage.class;
    }

    public Class<? extends ViewConfig> toListAllOrders() {
        return Pages.Orders.class;
    }
}
