package org.asuki.web.bean;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;

import lombok.Getter;

import org.asuki.common.annotation.Eager;
import org.primefaces.extensions.model.layout.LayoutOptions;
import org.slf4j.Logger;

@Eager
@Named
@ApplicationScoped
public class EagerBean implements Serializable {

    private static final long serialVersionUID = 1L;

    @Inject
    private Logger log;

    @Getter
    private LayoutOptions layoutOptions;

    @PostConstruct
    protected void initialize() {
        layoutOptions = new LayoutOptions();
        layoutOptions.addOption("key1", "value1");
        layoutOptions.addOption("key2", "value2");

        log.info("Created when the application starts");
    }

}
