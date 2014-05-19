package org.asuki.web.rs.exception;

import java.io.PrintWriter;
import java.io.Serializable;
import java.io.StringWriter;

import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@ToString
public class ErrorResponse implements Serializable {

    private static final long serialVersionUID = 1L;

    private String message;

    private String stackTrace;

    public ErrorResponse(Throwable e) {

        message = e.getMessage();

        StringWriter writer = new StringWriter();
        e.printStackTrace(new PrintWriter(writer));
        stackTrace = writer.toString();

    }
}
