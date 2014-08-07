package org.asuki.web.event;

import java.util.HashMap;
import java.util.Map;

import lombok.Getter;

public enum PaymentType {

    CREDIT("1"), DEBIT("2");

    @Getter
    private String value;

    private static Map<String, PaymentType> map = new HashMap<>();

    static {
        for (PaymentType paymentType : PaymentType.values()) {
            map.put(paymentType.getValue(), paymentType);
        }
    }

    private PaymentType(String value) {
        this.value = value;
    }

    public static PaymentType fromString(String value) {
        return map.get(value);
    }
}
