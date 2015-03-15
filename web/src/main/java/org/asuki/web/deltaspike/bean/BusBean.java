package org.asuki.web.deltaspike.bean;

import java.util.ArrayList;
import java.util.List;

import javax.enterprise.inject.Model;
import javax.inject.Inject;

import org.asuki.deltaspike.dto.LineDto;
import org.asuki.deltaspike.manager.BusManager;
import org.asuki.model.entity.deltaspike.Bus;
import org.asuki.web.deltaspike.annotation.Produced;

@Model
public class BusBean {

    @Inject
    private BusManager busManager;

    @Inject
    @Produced
    private LineDto lineDto;

    public List<Bus> getBuses() {
        if (lineDto.getDeparture() == null || lineDto.getArrival() == null) {
            return new ArrayList<Bus>();
        }

        return busManager
                .getBuses(lineDto.getDeparture(), lineDto.getArrival());
    }

}
