package org.asuki.web.bean;

import static com.google.common.base.Strings.isNullOrEmpty;

import javax.enterprise.inject.Model;
import javax.inject.Inject;

import lombok.Getter;
import lombok.Setter;

import org.asuki.common.Constants.Language;
import org.asuki.service.TranslatorService;

@Model
public class TranslateBean {

    @Inject
    private TranslatorService translator;

    @Setter
    @Getter
    private Language[] languages = Language.values();

    @Setter
    @Getter
    private Language language = Language.JAPAN;

    @Setter
    @Getter
    private String inputText;

    @Setter
    @Getter
    private String translatedText;

    public void translate() {
        if (!isNullOrEmpty(inputText)) {
            translatedText = translator.translate(inputText, language);
        }
    }

}
