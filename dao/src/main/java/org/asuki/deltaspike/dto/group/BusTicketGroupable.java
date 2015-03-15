package org.asuki.deltaspike.dto.group;

import org.apache.deltaspike.core.api.scope.ConversationSubGroup;

public interface BusTicketGroupable {

    @ConversationSubGroup(subGroup = { BusDtoGroup.class, TicketDtoGroup.class,
            SeatsDtoGroup.class, OrderDtoGroup.class })
    public interface DateSubGroupable extends BusTicketGroupable {
    }

    @ConversationSubGroup(subGroup = { SeatsDtoGroup.class, OrderDtoGroup.class })
    public interface SeatsSubGroupable extends BusTicketGroupable {
    }
}
