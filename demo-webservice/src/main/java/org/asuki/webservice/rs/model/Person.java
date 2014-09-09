package org.asuki.webservice.rs.model;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Person {

    @Pattern(regexp = "[0-9]+", message = "{person.id.pattern}")
    private String id;

    @Size(min = 2, max = 50, message = "{person.name.size}")
    private String name;

}
