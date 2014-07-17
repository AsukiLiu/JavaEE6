package org.asuki.common.exception;

import static com.google.common.base.Strings.repeat;

import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.Map;
import java.util.TreeMap;

import lombok.Getter;

public class SystemException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    @Getter
    private ErrorCode errorCode;

    @Getter
    private final Map<String, Object> properties = new TreeMap<>();

    public SystemException(ErrorCode errorCode) {
        this.errorCode = errorCode;
    }

    public SystemException(String message, ErrorCode errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public SystemException(Throwable cause, ErrorCode errorCode) {
        super(cause);
        this.errorCode = errorCode;
    }

    public SystemException(String message, Throwable cause, ErrorCode errorCode) {
        super(message, cause);
        this.errorCode = errorCode;
    }

    public static SystemException wrap(Throwable exception) {
        return wrap(exception, null);
    }

    public static SystemException wrap(Throwable exception, ErrorCode errorCode) {
        if (exception instanceof SystemException) {
            SystemException e = (SystemException) exception;

            if (errorCode == null || errorCode == e.getErrorCode()) {
                return e;
            }
        }

        return new SystemException(exception.getMessage(), exception, errorCode);
    }

    public SystemException setErrorCode(ErrorCode errorCode) {
        this.errorCode = errorCode;
        return this;
    }

    @SuppressWarnings("unchecked")
    public <T> T get(String name) {
        return (T) properties.get(name);
    }

    public SystemException set(String name, Object value) {
        properties.put(name, value);
        return this;
    }

    @Override
    public void printStackTrace(PrintStream stream) {
        synchronized (stream) {
            printStackTrace(new PrintWriter(stream));
        }
    }

    @Override
    public void printStackTrace(PrintWriter writer) {
        synchronized (writer) {
            writer.println(this);

            writer.println(repeat("-", 30));

            if (errorCode != null) {
                writer.println(errorCode + ":" + errorCode.getClass().getName());
            }
            for (String key : properties.keySet()) {
                writer.println(key + "=[" + properties.get(key) + "]");
            }

            writer.println(repeat("-", 30));

            StackTraceElement[] trace = getStackTrace();
            for (int i = 0; i < trace.length; i++) {
                writer.println("at " + trace[i]);
            }

            Throwable cause = getCause();
            if (cause != null) {
                cause.printStackTrace(writer);
            }
            writer.flush();
        }
    }

}
