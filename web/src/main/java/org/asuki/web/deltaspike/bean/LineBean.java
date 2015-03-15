package org.asuki.web.deltaspike.bean;

import java.util.ArrayList;
import java.util.List;

import javax.enterprise.inject.Model;
import javax.inject.Inject;

import org.asuki.deltaspike.manager.LineManager;

@Model
public class LineBean {

    @Inject
    private LineManager lineManager;

    public List<String> getAllDepartures() {
        return lineManager.getAllDepartures();
    }

    public List<String> getAllArrivals(String departure) {
        if (departure == null) {
            return new ArrayList<String>();
        }
        return lineManager.getAllArrivals(departure);
    }

}
