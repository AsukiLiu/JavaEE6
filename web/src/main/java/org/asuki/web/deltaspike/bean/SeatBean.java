package org.asuki.web.deltaspike.bean;

import java.util.List;

import javax.enterprise.inject.Model;
import javax.inject.Inject;

import org.asuki.deltaspike.dto.TicketDto;
import org.asuki.deltaspike.manager.TicketManager;
import org.asuki.model.entity.deltaspike.Ticket;
import org.asuki.web.deltaspike.Utils;
import org.asuki.web.deltaspike.annotation.Produced;

@Model
public class SeatBean {

    @Inject
    private TicketManager ticketManager;

    @Inject
    @Produced
    private TicketDto ticketDto;

    public List<String> getFreeSeats() {
        if (ticketDto == null || ticketDto.getBusDto() == null) {
            return null;
        }

        List<Ticket> busTickets = ticketManager.getBusTickets(ticketDto
                .getBusDto().getId());

        List<String> freeSeats = Utils.getAllBusSeats();
        for (Ticket ticket : busTickets) {
            freeSeats.remove(ticket.getSeatNumber());
        }

        return freeSeats;
    }

}
