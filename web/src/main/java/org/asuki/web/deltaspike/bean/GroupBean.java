package org.asuki.web.deltaspike.bean;

import javax.enterprise.inject.Model;
import javax.inject.Inject;

import org.apache.deltaspike.core.spi.scope.conversation.GroupedConversationManager;
import org.asuki.deltaspike.dto.group.BusTicketGroupable;
import org.asuki.deltaspike.dto.group.BusTicketGroupable.DateSubGroupable;
import org.asuki.deltaspike.dto.group.BusTicketGroupable.SeatsSubGroupable;

@Model
public class GroupBean {

    @Inject
    private GroupedConversationManager conversationManager;

    public void resetBusLine() {
        conversationManager.closeConversationGroup(BusTicketGroupable.class);
    }

    public void resetDate() {
        conversationManager.closeConversationGroup(DateSubGroupable.class);
    }

    public void resetSeats() {
        conversationManager.closeConversationGroup(SeatsSubGroupable.class);
    }

}
