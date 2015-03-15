package org.asuki.deltaspike.dto.group;

import java.util.List;

import javax.inject.Named;

import lombok.NoArgsConstructor;

import org.apache.deltaspike.core.api.scope.ConversationGroup;
import org.apache.deltaspike.core.api.scope.GroupedConversationScoped;
import org.asuki.deltaspike.dto.SeatsDto;

@Named
@GroupedConversationScoped
@ConversationGroup(BusTicketGroupable.class)
@NoArgsConstructor
public class SeatsDtoGroup extends SeatsDto {

    private static final long serialVersionUID = 1L;

    public SeatsDtoGroup(List<String> chosenSeats) {
        super(chosenSeats);
    }

}
