package org.asuki.web.deltaspike;

import static java.util.Arrays.asList;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.enterprise.inject.Model;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

@Model
public class Utils {

    @Inject
    private FacesContext facesContext;

    public boolean isGroup() {
        return facesContext.getViewRoot().getViewId().contains("group");
    }

    public String formatDate(Date date) {
        if (date == null) {
            return "";
        }
        SimpleDateFormat formater = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        return formater.format(date);
    }

    public static List<String> getAllBusSeats() {
        List<String> allSeats = new ArrayList<>();

        String[] lines = { "A", "B", "C", "D" };
        for (int i = 1; i <= 12; i++) {
            for (String line : lines) {
                allSeats.add(i + line);
            }
        }
        allSeats.removeAll(asList("8C", "8D"));

        return allSeats;
    }

}
