package org.asuki.deltaspike.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class OrderDto extends BaseDto {

    private static final long serialVersionUID = 1L;

    private List<TicketDto> ticketsDto;

    private String paymentType;

    private Boolean paid;

    private Double toPay;

}
