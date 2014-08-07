package org.asuki.web.event;

import java.math.BigDecimal;
import java.util.Date;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class PaymentEvent {

    private PaymentType type;

    private BigDecimal amount;

    private Date datetime;

}
