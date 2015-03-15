package org.asuki.deltaspike.manager;

import java.util.List;

import javax.inject.Inject;

import org.asuki.deltaspike.repository.TicketRepository;
import org.asuki.model.entity.deltaspike.Ticket;

public class TicketManager {

    @Inject
    private TicketRepository ticketRepository;

    public List<Ticket> getBusTickets(long busId) {
        return ticketRepository.getBusTickets(busId);
    }

}
