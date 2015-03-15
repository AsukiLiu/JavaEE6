package org.asuki.deltaspike.manager;

import java.util.List;

import javax.inject.Inject;

import org.asuki.deltaspike.repository.LineReporitory;

public class LineManager {

    @Inject
    private LineReporitory lineReporitory;

    public List<String> getAllDepartures() {
        return lineReporitory.getAllDepartures();
    }

    public List<String> getAllArrivals(String departure) {
        return lineReporitory.getAllArrivals(departure);
    }

}
