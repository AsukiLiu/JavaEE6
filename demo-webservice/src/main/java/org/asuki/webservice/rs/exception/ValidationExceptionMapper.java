package org.asuki.webservice.rs.exception;

import static javax.ws.rs.core.Response.status;
import static javax.ws.rs.core.Response.Status.PRECONDITION_FAILED;

import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import org.hibernate.validator.method.MethodConstraintViolation;
import org.hibernate.validator.method.MethodConstraintViolationException;

@Provider
public class ValidationExceptionMapper implements
        ExceptionMapper<MethodConstraintViolationException> {

    @Override
    public Response toResponse(MethodConstraintViolationException ex) {
        Map<String, String> errors = new HashMap<>();

        for (MethodConstraintViolation<?> methodConstraintViolation : ex
                .getConstraintViolations()) {
            errors.put(methodConstraintViolation.getParameterName(),
                    methodConstraintViolation.getMessage());
        }

        return status(PRECONDITION_FAILED).entity(errors).build();
    }
}
