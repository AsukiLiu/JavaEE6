package org.asuki.bv;

import static org.asuki.common.Constants.Systems.LINE_SEPARATOR;
import static org.testng.collections.Lists.newArrayList;

import java.util.List;
import java.util.Set;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import javax.validation.groups.Default;

import com.google.common.base.Joiner;

@ApplicationScoped
public class CustomValidator {

    @Inject
    private Validator validator;

    public <T> void validate(T t, Class<?>... groups) throws Exception {

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

        throw new Exception(Joiner.on(LINE_SEPARATOR).skipNulls()
                .join(errorMessages));
    }

}
