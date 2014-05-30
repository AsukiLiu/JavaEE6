package org.asuki.webservice.rs.exception;

import java.io.PrintWriter;
import java.io.Serializable;
import java.io.StringWriter;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@ToString
public class ErrorResponse implements Serializable {

    private static final long serialVersionUID = 1L;

    @Getter
    private String message;

    @Getter
    private String stackTrace;

    public ErrorResponse(Throwable e) {

        message = e.getMessage();

        StringWriter writer = new StringWriter();
        e.printStackTrace(new PrintWriter(writer));
        stackTrace = writer.toString();

    }
}
