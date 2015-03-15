package org.asuki.deltaspike.repository;

import java.util.List;

import org.apache.deltaspike.data.api.AbstractEntityRepository;
import org.apache.deltaspike.data.api.Repository;
import org.apache.deltaspike.data.api.criteria.CriteriaSupport;
import org.asuki.model.entity.deltaspike.Bus;
import org.asuki.model.entity.deltaspike.Bus_;
import org.asuki.model.entity.deltaspike.Ticket;
import org.asuki.model.entity.deltaspike.Ticket_;

@Repository(forEntity = Ticket.class)
public abstract class TicketRepository extends
        AbstractEntityRepository<Ticket, Long> implements
        CriteriaSupport<Ticket> {

    public List<Ticket> getBusTickets(long busId) {
        return criteria()
                .join(Ticket_.bus, where(Bus.class).eq(Bus_.id, busId))
                .getResultList();
    }
}
