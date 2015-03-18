package org.asuki.deltaSpike.message;

import java.util.Locale;

import javax.enterprise.context.RequestScoped;

import lombok.Setter;

import org.apache.deltaspike.core.api.message.LocaleResolver;
import org.asuki.common.Constants.Language;

@RequestScoped
@Custom
public class CustomLocaleResolver implements LocaleResolver {

    private static final long serialVersionUID = 1L;

    @Setter
    private Language language;

    @Override
    public Locale getLocale() {
        return language.equals(Language.JAPAN) ? Locale.JAPAN : Locale.CHINA;
    }
}
