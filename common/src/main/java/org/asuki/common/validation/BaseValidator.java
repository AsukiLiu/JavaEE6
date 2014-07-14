package org.asuki.common.validation;

import static com.google.common.base.Optional.absent;

import com.google.common.base.Optional;

public abstract class BaseValidator<T> {

    private Optional<BaseValidator<T>> nextValidator = absent();

    public final void validate(T toValidate, ValidationResult validationResult) {

        validateSpecific(toValidate, validationResult);

        if (nextValidator.isPresent()) {
            nextValidator.get().validate(toValidate, validationResult);
        }
    }

    public BaseValidator<T> and(BaseValidator<T> nextValidator) {
        this.nextValidator = Optional.of(nextValidator);
        return this;
    }

    protected abstract void validateSpecific(T toValidate,
            ValidationResult validationResult);
}
