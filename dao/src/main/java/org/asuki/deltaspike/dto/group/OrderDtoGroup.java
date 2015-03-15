package org.asuki.deltaspike.dto.group;

import java.util.List;

import javax.inject.Named;

import lombok.NoArgsConstructor;

import org.apache.deltaspike.core.api.scope.ConversationGroup;
import org.apache.deltaspike.core.api.scope.GroupedConversationScoped;
import org.asuki.deltaspike.dto.OrderDto;
import org.asuki.deltaspike.dto.TicketDto;

@Named
@GroupedConversationScoped
@ConversationGroup(BusTicketGroupable.class)
@NoArgsConstructor
public class OrderDtoGroup extends OrderDto {

    private static final long serialVersionUID = 1L;

    public OrderDtoGroup(List<TicketDto> ticketsDto, String paymentType,
            boolean paid, Double toPay) {
        super(ticketsDto, paymentType, paid, toPay);
    }

}
