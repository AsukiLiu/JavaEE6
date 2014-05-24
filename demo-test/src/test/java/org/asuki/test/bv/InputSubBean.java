package org.asuki.test.bv;

import java.math.BigDecimal;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Setter
@Getter
public class InputSubBean {

    @Digits(integer = 5, fraction = 0)
    @NotNull
    private BigDecimal data;

}
