package org.asuki.deltaSpike.message;

import org.apache.deltaspike.core.api.message.MessageBundle;
import org.apache.deltaspike.core.api.message.MessageTemplate;

@MessageBundle
public interface InternationalMessages {

    @MessageTemplate(value = "{unknown}")
    String unknownText();
}
