package org.asuki.deltaspike.repository;

import java.util.List;

import org.apache.deltaspike.data.api.EntityRepository;
import org.apache.deltaspike.data.api.Query;
import org.apache.deltaspike.data.api.Repository;
import org.asuki.model.entity.deltaspike.Line;

@Repository(forEntity = Line.class)
public interface LineReporitory extends EntityRepository<Line, Long> {

    @Query("SELECT l.departure FROM Line l GROUP BY l.departure")
    public List<String> getAllDepartures();

    @Query("SELECT l.arrival FROM Line l WHERE l.departure = ?1")
    public List<String> getAllArrivals(String departure);

    @Query("SELECT l.id FROM Line l WHERE l.departure = ?1 AND l.arrival = ?2")
    public Long getLineId(String departure, String arrival);
}
