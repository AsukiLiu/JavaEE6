package org.asuki.web.listener;

import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CustomPhaseListener implements PhaseListener {

    private Logger log = LoggerFactory.getLogger(getClass());

    private static final long serialVersionUID = 1L;

    @Override
    public void beforePhase(PhaseEvent event) {

        log.info("[BEFORE]" + event.getPhaseId());
    }

    @Override
    public void afterPhase(PhaseEvent event) {

        log.info("[AFTER]" + event.getPhaseId());
    }

    @Override
    public PhaseId getPhaseId() {

        return PhaseId.ANY_PHASE;
    }

}
