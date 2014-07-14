package org.asuki.common.validation;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class ValidateEndsWith extends BaseValidator<String> {

    private String target;

    private String errorMessage;

    @Override
    protected void validateSpecific(String toValidate,
            ValidationResult validationResult) {

        if (toValidate.endsWith(target)) {
            validationResult.addViolation(errorMessage);
        }
    }
}
