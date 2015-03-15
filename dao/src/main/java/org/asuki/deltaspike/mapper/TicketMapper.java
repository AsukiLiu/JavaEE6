package org.asuki.deltaspike.mapper;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.apache.deltaspike.data.api.mapping.SimpleQueryInOutMapperBase;
import org.asuki.deltaspike.dto.TicketDto;
import org.asuki.model.entity.deltaspike.Bus;
import org.asuki.model.entity.deltaspike.Ticket;

public class TicketMapper extends SimpleQueryInOutMapperBase<Ticket, TicketDto> {

    @Inject
    private BusMapper busMapper;

    @Override
    protected Object getPrimaryKey(TicketDto dto) {
        return dto.getId();
    }

    @Override
    protected TicketDto toDto(Ticket entity) {
        TicketDto ticketDto = new TicketDto(busMapper.toDto(entity.getBus()),
                entity.getSeatNumber(), entity.getInFirstClass());
        ticketDto.setId(entity.getId());
        return ticketDto;
    }

    @Override
    protected Ticket toEntity(Ticket entity, TicketDto dto) {
        entity.setBus(busMapper.toEntity(new Bus(), dto.getBusDto()));
        entity.setSeatNumber(dto.getSeatNumber());
        entity.setInFirstClass(dto.getInFirstClass());
        entity.setId(dto.getId());

        return entity;
    }

    public List<Ticket> toEntityList(List<TicketDto> dtoList) {
        List<Ticket> entityList = new ArrayList<>(dtoList.size());
        for (TicketDto ticket : dtoList) {
            entityList.add(toEntity(new Ticket(), ticket));
        }

        return entityList;
    }

}
