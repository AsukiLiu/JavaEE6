package org.asuki.web.deltaspike.service;

import org.asuki.common.Constants.Language;

public interface TranslatorService {

    String translate(String messageText, Language language);

}
