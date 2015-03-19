package org.asuki.deltaSpike.jmx;

import javax.enterprise.context.ApplicationScoped;

import org.apache.deltaspike.core.api.jmx.JmxManaged;
import org.apache.deltaspike.core.api.jmx.MBean;

@ApplicationScoped
@MBean(description = "Manage language translations.")
public class TranslationMBean {

    private int translations = 0;

    public void addTranslation() {
        translations++;
    }

    @JmxManaged(description = "Get number of all translations.")
    public int getTranslations() {
        return translations;
    }

    @JmxManaged(description = "Reset the translations.")
    public void reset() {
        translations = 0;
    }

}
