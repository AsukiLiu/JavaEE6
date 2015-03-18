package org.asuki.service;

import org.asuki.common.Constants.Language;

public interface TranslatorService {

    String translate(String messageText, Language language);

}
