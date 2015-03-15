package org.asuki.deltaspike.dto;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class BaseDto implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
}
