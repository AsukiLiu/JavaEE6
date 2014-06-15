package org.asuki.common.cdi.extension;

import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.AfterDeploymentValidation;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.inject.spi.Extension;
import javax.enterprise.inject.spi.ProcessBean;

import org.asuki.common.annotation.Eager;

public class EagerExtension implements Extension {

    private List<Bean<?>> eagerBeansList = new ArrayList<>();

    public <T> void collectBeans(@Observes ProcessBean<T> event) {

        if (event.getAnnotated().isAnnotationPresent(Eager.class)
                && event.getAnnotated().isAnnotationPresent(
                        ApplicationScoped.class)) {
            eagerBeansList.add(event.getBean());
        }
    }

    public void loadBeans(@Observes AfterDeploymentValidation event,
            BeanManager beanManager) {

        for (Bean<?> bean : eagerBeansList) {
            beanManager.getReference(bean, bean.getBeanClass(),
                    beanManager.createCreationalContext(bean)).toString();
        }
    }

}