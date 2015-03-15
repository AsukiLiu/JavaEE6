package org.asuki.model.entity.deltaspike;

import javax.persistence.Entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Line extends Base {

    private static final long serialVersionUID = 1L;

    private String departure;

    private String arrival;

    private Integer price;

}
