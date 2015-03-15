package org.asuki.deltaspike.repository;

import java.util.List;

import org.apache.deltaspike.data.api.AbstractEntityRepository;
import org.apache.deltaspike.data.api.Repository;
import org.apache.deltaspike.data.api.criteria.CriteriaSupport;
import org.asuki.model.entity.deltaspike.Bus;
import org.asuki.model.entity.deltaspike.Bus_;
import org.asuki.model.entity.deltaspike.Line;
import org.asuki.model.entity.deltaspike.Line_;

@Repository(forEntity = Bus.class)
public abstract class BusReporitory extends AbstractEntityRepository<Bus, Long>
        implements CriteriaSupport<Bus> {

    public List<Bus> getDates(String departure, String arrival) {
        return criteria()
                .join(Bus_.line,
                        where(Line.class).eq(Line_.departure, departure).eq(
                                Line_.arrival, arrival)).orderAsc(Bus_.date)
                .getResultList();
    }
}
