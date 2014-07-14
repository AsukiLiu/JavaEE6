package org.asuki.common.validation;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class ValidateStartsWith extends BaseValidator<String> {

    private String target;

    private String errorMessage;

    @Override
    protected void validateSpecific(String toValidate,
            ValidationResult validationResult) {

        if (toValidate.startsWith(target)) {
            validationResult.addViolation(errorMessage);
        }
    }
}
