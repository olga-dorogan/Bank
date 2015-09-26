package com.custom.app.exception;

/**
 * Created by olga on 17.09.15.
 */
public class BankDAOException extends BankException {
    public BankDAOException() {
        super();
    }

    public BankDAOException(String message) {
        super(message);
    }

    public BankDAOException(Exception cause) {
        super(cause);
    }

    public BankDAOException(String message, Exception e) {
        super(message, e);
    }
}
