package org.asuki.deltaspike.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public abstract class LineDto extends BaseDto {

    private static final long serialVersionUID = 1L;

    private String departure;

    private String arrival;

    private Integer price;

}
