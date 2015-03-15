package org.asuki.deltaspike.dto;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class BusDto extends BaseDto {

    private static final long serialVersionUID = 1L;

    private LineDto lineDto;

    private Date date;

}
