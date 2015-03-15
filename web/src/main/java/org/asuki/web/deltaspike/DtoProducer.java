package org.asuki.web.deltaspike;

import javax.enterprise.inject.Produces;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

import org.apache.deltaspike.core.api.scope.ConversationGroup;
import org.asuki.deltaspike.dto.BusDto;
import org.asuki.deltaspike.dto.LineDto;
import org.asuki.deltaspike.dto.OrderDto;
import org.asuki.deltaspike.dto.SeatsDto;
import org.asuki.deltaspike.dto.TicketDto;
import org.asuki.deltaspike.dto.access.BusDtoAccess;
import org.asuki.deltaspike.dto.access.LineDtoAccess;
import org.asuki.deltaspike.dto.access.OrderDtoAccess;
import org.asuki.deltaspike.dto.access.SeatsDtoAccess;
import org.asuki.deltaspike.dto.access.TicketDtoAccess;
import org.asuki.deltaspike.dto.group.BusDtoGroup;
import org.asuki.deltaspike.dto.group.BusTicketGroupable;
import org.asuki.deltaspike.dto.group.LineDtoGroup;
import org.asuki.deltaspike.dto.group.OrderDtoGroup;
import org.asuki.deltaspike.dto.group.SeatsDtoGroup;
import org.asuki.deltaspike.dto.group.TicketDtoGroup;
import org.asuki.web.deltaspike.annotation.Produced;

public class DtoProducer {

    @Inject
    private BusDtoAccess busDtoAccess;

    @Inject
    @ConversationGroup(BusTicketGroupable.class)
    private BusDtoGroup busDtoGroup;

    @Inject
    private LineDtoAccess lineDtoAccess;

    @Inject
    @ConversationGroup(BusTicketGroupable.class)
    private LineDtoGroup lineDtoGroup;

    @Inject
    private OrderDtoAccess orderDtoAccess;

    @Inject
    @ConversationGroup(BusTicketGroupable.class)
    private OrderDtoGroup orderDtoGroup;

    @Inject
    private SeatsDtoAccess seatsDtoAccess;

    @Inject
    @ConversationGroup(BusTicketGroupable.class)
    private SeatsDtoGroup seatsDtoGroup;

    @Inject
    private TicketDtoAccess ticketDtoAccess;

    @Inject
    @ConversationGroup(BusTicketGroupable.class)
    private TicketDtoGroup ticketDtoGroup;

    @Inject
    private FacesContext facesContext;

    public boolean isGroup() {
        return facesContext.getViewRoot().getViewId().contains("group");
    }

    @Produces
    @Produced
    public BusDto getBusDto() {
        return isGroup() ? busDtoGroup : busDtoAccess;
    }

    @Produces
    @Produced
    public LineDto getLineDto() {
        return isGroup() ? lineDtoGroup : lineDtoAccess;
    }

    @Produces
    @Produced
    public OrderDto getOrderDto() {
        return isGroup() ? orderDtoGroup : orderDtoAccess;
    }

    @Produces
    @Produced
    public SeatsDto getSeatsDto() {
        return isGroup() ? seatsDtoGroup : seatsDtoAccess;
    }

    @Produces
    @Produced
    public TicketDto getTicketDto() {
        return isGroup() ? ticketDtoGroup : ticketDtoAccess;
    }
}
