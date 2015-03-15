package org.asuki.model.entity.deltaspike;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class Bus implements Serializable {

    private static final long serialVersionUID = 1L;

    // How Bus_.class generates the fields in @MappedSuperclass ?
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    private Line line;

    @Temporal(TemporalType.TIMESTAMP)
    private Date date;

    public Bus(Line line, Date date) {
        this.line = line;
        this.date = date;
    }

}
