package org.asuki.web.rs.exception;

import static javax.ws.rs.core.Response.Status.NOT_FOUND;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class IllegalArgumentExceptionMapper implements
        ExceptionMapper<IllegalArgumentException> {

    @Override
    public Response toResponse(IllegalArgumentException e) {

        // return Response.ok(e.getMessage()).type(MediaType.TEXT_PLAIN).build();

        return Response.status(NOT_FOUND)
                // .entity("{message:" + e.getMessage() + "}")
                .entity(new ErrorResponse(e))
                .type(MediaType.TEXT_PLAIN)
                .build();

    }
}
