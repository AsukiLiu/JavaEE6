package org.asuki.deltaspike.dto.group;

import javax.inject.Named;

import lombok.NoArgsConstructor;

import org.apache.deltaspike.core.api.scope.ConversationGroup;
import org.apache.deltaspike.core.api.scope.GroupedConversationScoped;
import org.asuki.deltaspike.dto.BusDto;
import org.asuki.deltaspike.dto.TicketDto;

@Named
@GroupedConversationScoped
@ConversationGroup(BusTicketGroupable.class)
@NoArgsConstructor
public class TicketDtoGroup extends TicketDto {

    private static final long serialVersionUID = 1L;

    public TicketDtoGroup(BusDto busDto, String seatNumber,
            boolean isInFirstClass) {
        super(busDto, seatNumber, isInFirstClass);
    }

}
