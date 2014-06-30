package org.asuki.model.listener;

import static java.util.Calendar.DAY_OF_YEAR;
import static java.util.Calendar.YEAR;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.persistence.PostLoad;
import javax.persistence.PostPersist;
import javax.persistence.PostUpdate;

import org.asuki.model.entity.Employee;

public class CustomListener {

    @PostLoad
    @PostPersist
    @PostUpdate
    private void calculateAge(Employee employee) {
        if (employee.getBirthday() == null) {
            employee.setAge(null);
            return;
        }

        Calendar birth = new GregorianCalendar();
        birth.setTime(employee.getBirthday());
        Calendar now = new GregorianCalendar();
        now.setTime(new Date());

        int adjust = now.get(DAY_OF_YEAR) - birth.get(DAY_OF_YEAR) < 0 ? -1 : 0;

        employee.setAge(now.get(YEAR) - birth.get(YEAR) + adjust);
    }

}
