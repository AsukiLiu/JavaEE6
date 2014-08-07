package org.asuki.web.observer;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.SessionScoped;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.Produces;
import javax.inject.Named;

import org.asuki.web.event.PaymentEvent;
import org.asuki.web.qualifier.Credit;
import org.asuki.web.qualifier.Debit;

@SessionScoped
public class PaymentObserver implements Serializable {

    private static final long serialVersionUID = 1L;

    private List<PaymentEvent> payments = new ArrayList<>();

    @Produces
    @Named
    public List<PaymentEvent> getPayments() {
        return payments;
    }

    public void onCreditPaymentEvent(@Observes @Credit PaymentEvent event) {
        payments.add(event);
    }

    public void onDebitPaymentEvent(@Observes @Debit PaymentEvent event) {
        payments.add(event);
    }

}
