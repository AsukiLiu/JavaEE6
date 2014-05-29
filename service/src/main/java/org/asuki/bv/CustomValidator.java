package org.asuki.bv;

import static com.google.common.collect.Lists.newArrayList;
import static org.asuki.common.Constants.Systems.LINE_SEPARATOR;

import java.util.List;
import java.util.Set;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import javax.validation.groups.Default;

import org.asuki.exception.ServiceException;

import com.google.common.base.Joiner;

@ApplicationScoped
public class CustomValidator {

    @Inject
    private Validator validator;

    public <T> void validate(T t, Class<?>... groups) throws ServiceException {

        List<Class<?>> list = newArrayList();
        list.add(Default.class);

        for (Class<?> clazz : groups) {
            list.add(clazz);
        }

        Set<ConstraintViolation<T>> violations = validator.validate(t,
                list.toArray(new Class<?>[list.size()]));

        if (violations.isEmpty()) {
            return;
        }

        List<String> errorMessages = newArrayList();
        for (ConstraintViolation<T> violation : violations) {

            errorMessages.add(violation.getInvalidValue() + ":"
                    + violation.getMessage());
        }

        String message = Joiner.on(LINE_SEPARATOR).skipNulls()
                .join(errorMessages);

        throw new ServiceException(message) {
            private static final long serialVersionUID = 1L;
        };

    }

}
