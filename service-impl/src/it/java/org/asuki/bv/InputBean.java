package org.asuki.bv;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.*;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class InputBean {

    @Size(min = 3, max = 5)
    @Pattern(regexp = "ABC*")
    @NotNull
    private String data;

    @Valid
    private List<InputSubBean> beans;

}
