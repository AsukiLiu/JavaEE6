package org.asuki.exception;

import javax.ejb.ApplicationException;

@ApplicationException(rollback = true)
public abstract class ServiceException extends Exception {

    private static final long serialVersionUID = 1L;

    protected ServiceException() {
        super();
    }

    protected ServiceException(String message) {
        super(message);
    }

    protected ServiceException(Throwable e) {
        super(e);
    }

    protected ServiceException(String message, Throwable e) {
        super(message, e);
    }

}
