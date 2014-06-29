package org.asuki.model.jackson;

import java.util.Set;

import javax.validation.Valid;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Tool implements JsonItem {

    private static final long serialVersionUID = 1L;

    private String toolName;

    private String version;

    private boolean openSource;

    @Valid
    private Set<String> plugins;

}
