package org.asuki.deltaspike.dto.access;

import javax.inject.Named;

import lombok.NoArgsConstructor;

import org.apache.deltaspike.core.api.scope.ViewAccessScoped;
import org.asuki.deltaspike.dto.BusDto;
import org.asuki.deltaspike.dto.TicketDto;

@Named
@ViewAccessScoped
@NoArgsConstructor
public class TicketDtoAccess extends TicketDto {

    private static final long serialVersionUID = 1L;

    public TicketDtoAccess(BusDto busDto, String seatNumber,
            boolean isInFirstClass) {
        super(busDto, seatNumber, isInFirstClass);
    }

}
