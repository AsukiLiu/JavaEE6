package org.asuki.web.deltaspike.service;

import javax.decorator.Decorator;
import javax.decorator.Delegate;
import javax.inject.Inject;

import org.asuki.common.Constants.Language;
import org.asuki.deltaSpike.jmx.TranslationMBean;

@Decorator
public abstract class TranslatorDecorator implements TranslatorService {

    @Inject
    @Delegate
    private TranslatorService translator;

    @Inject
    private TranslationMBean translationMBean;

    @Override
    public String translate(String messageText, Language language) {
        translationMBean.addTranslation();

        return translator.translate(messageText, language);
    }

}
