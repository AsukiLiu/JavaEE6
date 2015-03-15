package org.asuki.deltaspike.dto.group;

import java.util.Date;

import lombok.NoArgsConstructor;

import org.apache.deltaspike.core.api.scope.ConversationGroup;
import org.apache.deltaspike.core.api.scope.GroupedConversationScoped;
import org.asuki.deltaspike.dto.BusDto;
import org.asuki.deltaspike.dto.LineDto;

@GroupedConversationScoped
@ConversationGroup(BusTicketGroupable.class)
@NoArgsConstructor
public class BusDtoGroup extends BusDto {

    private static final long serialVersionUID = 1L;

    public BusDtoGroup(LineDto lineDto, Date date) {
        super(lineDto, date);
    }

}
