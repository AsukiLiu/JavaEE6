package org.asuki.deltaspike.dto.group;

import javax.inject.Named;

import lombok.NoArgsConstructor;

import org.apache.deltaspike.core.api.scope.ConversationGroup;
import org.apache.deltaspike.core.api.scope.GroupedConversationScoped;
import org.asuki.deltaspike.dto.LineDto;

@Named
@GroupedConversationScoped
@ConversationGroup(BusTicketGroupable.class)
@NoArgsConstructor
public class LineDtoGroup extends LineDto {

    private static final long serialVersionUID = 1L;

    public LineDtoGroup(String departure, String arrival, int price) {
        super(departure, arrival, price);
    }

}
