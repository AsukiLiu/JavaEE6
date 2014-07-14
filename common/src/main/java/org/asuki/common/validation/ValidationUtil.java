package org.asuki.common.validation;

public final class ValidationUtil {

    public static ValidationResult validate(String toValidate) {
        ValidationResult result = new ValidationResult();
        getValidationChain().validate(toValidate, result);
        return result;
    }

    private static BaseValidator<String> getValidationChain() {
        return new ValidateStartsWith("A", "ERROR:Starts with A")
                .and(new ValidateEndsWith("B", "ERROR:Ends with B"));
    }
}
