package org.asuki.deltaspike.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class TicketDto extends BaseDto {

    private static final long serialVersionUID = 1L;

    private BusDto busDto;

    private String seatNumber;

    private Boolean inFirstClass;

}
