package org.asuki.common.exception;

public class CommonError extends Error {

    private static final long serialVersionUID = 1L;

    public static final String CANNOT_BE_INSTANCED = "インスタンス化できない";

    public CommonError(String message) {
        super(message);
    }
}
