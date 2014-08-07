package org.asuki.web.bean;

import static org.asuki.web.event.PaymentType.DEBIT;
import static org.asuki.web.event.PaymentType.fromString;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.enterprise.context.SessionScoped;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.inject.Named;

import lombok.Getter;
import lombok.Setter;

import org.asuki.web.event.PaymentEvent;
import org.asuki.web.qualifier.Credit;
import org.asuki.web.qualifier.Debit;

@Named
@SessionScoped
public class EventBean implements Serializable {

    private static final long serialVersionUID = 1L;

    @Inject
    @Credit
    private Event<PaymentEvent> creditEvent;

    @Inject
    @Debit
    private Event<PaymentEvent> debitEvent;

    @Setter
    @Getter
    private BigDecimal amount = new BigDecimal(1000.0);

    @Setter
    @Getter
    private String paymentOption = DEBIT.getValue();

    public void pay() {
        PaymentEvent currentPayment = new PaymentEvent();
        currentPayment.setType(fromString(paymentOption));
        currentPayment.setAmount(amount);
        currentPayment.setDatetime(new Date());

        switch (currentPayment.getType()) {
        case DEBIT:
            debitEvent.fire(currentPayment);
            break;
        case CREDIT:
            creditEvent.fire(currentPayment);
            break;
        default:
            break;
        }
    }

    public void reset() {
        amount = null;
        paymentOption = "";
    }

}
