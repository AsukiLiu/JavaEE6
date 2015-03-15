package org.asuki.deltaspike.manager;

import java.util.List;

import javax.inject.Inject;

import org.asuki.deltaspike.repository.BusReporitory;
import org.asuki.model.entity.deltaspike.Bus;

public class BusManager {

    @Inject
    private BusReporitory busReporitory;

    public List<Bus> getBuses(String departure, String arrival) {
        return busReporitory.getDates(departure, arrival);
    }

}
