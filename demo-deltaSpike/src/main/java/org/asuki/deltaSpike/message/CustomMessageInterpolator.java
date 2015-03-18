package org.asuki.deltaSpike.message;

import javax.enterprise.context.RequestScoped;
import java.io.Serializable;
import java.text.MessageFormat;
import java.util.Locale;

import lombok.Setter;

import org.apache.deltaspike.core.api.message.MessageInterpolator;

@RequestScoped
@Custom
public class CustomMessageInterpolator implements MessageInterpolator {

    private static final long serialVersionUID = 1L;

    @Setter
    private String originalText;

    @Override
    public String interpolate(String messageText, Serializable[] arguments,
            Locale locale) {

        if (messageText.contains(originalText)) {
            return "";
        }

        return MessageFormat.format(messageText, (Object[]) arguments);
    }
}
