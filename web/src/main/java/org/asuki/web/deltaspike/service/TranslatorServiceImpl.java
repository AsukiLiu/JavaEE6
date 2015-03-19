package org.asuki.web.deltaspike.service;

import static com.google.common.base.Strings.isNullOrEmpty;
import static java.lang.String.format;

import javax.inject.Inject;

import org.apache.deltaspike.core.api.message.Message;
import org.apache.deltaspike.core.api.message.MessageContext;
import org.asuki.common.Constants.Language;
import org.asuki.deltaSpike.message.Custom;
import org.asuki.deltaSpike.message.CustomLocaleResolver;
import org.asuki.deltaSpike.message.CustomMessageInterpolator;
import org.asuki.deltaSpike.message.InternationalMessages;

public class TranslatorServiceImpl implements TranslatorService {

    private static final String MESSAGE_SOURCE = "org.asuki.deltaSpike.message.InternationalMessages";
    private static final String MESSAGE_TEMPLATE = "{%s}";

    @Inject
    private MessageContext messageContext;

    @Inject
    private InternationalMessages messages;

    @Inject
    @Custom
    private CustomMessageInterpolator interpolator;

    @Inject
    @Custom
    private CustomLocaleResolver localeResolver;

    @Override
    public String translate(String messageText, Language language) {

        interpolator.setOriginalText(messageText);
        localeResolver.setLanguage(language);

        Message message = messageContext.messageSource(MESSAGE_SOURCE)
                .messageInterpolator(interpolator)
                .localeResolver(localeResolver).message();

        message = message.template(format(MESSAGE_TEMPLATE, messageText
                .toLowerCase().trim()));

        if (isNullOrEmpty(message.toString())) {
            return messages.unknownText();
        }

        return message.toString();
    }
}
