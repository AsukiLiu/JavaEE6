package org.asuki.model.entity.deltaspike;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Ticket extends Base {

    private static final long serialVersionUID = 1L;

    @ManyToOne
    private Bus bus;

    private String seatNumber;

    private Boolean inFirstClass;

}
