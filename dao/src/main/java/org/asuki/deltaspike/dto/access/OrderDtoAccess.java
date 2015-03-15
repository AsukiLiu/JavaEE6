package org.asuki.deltaspike.dto.access;

import java.util.List;

import javax.inject.Named;

import lombok.NoArgsConstructor;

import org.apache.deltaspike.core.api.scope.ViewAccessScoped;
import org.asuki.deltaspike.dto.OrderDto;
import org.asuki.deltaspike.dto.TicketDto;

@Named
@ViewAccessScoped
@NoArgsConstructor
public class OrderDtoAccess extends OrderDto {

    private static final long serialVersionUID = 1L;

    public OrderDtoAccess(List<TicketDto> ticketsDto, String paymentType,
            boolean paid, Double toPay) {
        super(ticketsDto, paymentType, paid, toPay);
    }

}
