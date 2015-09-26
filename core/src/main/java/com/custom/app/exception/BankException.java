package com.custom.app.exception;

/**
 * Created by olga on 17.09.15.
 */
public class BankException extends RuntimeException {
    public BankException() {
        super();
    }

    public BankException(String message) {
        super(message);
    }

    public BankException(Exception cause) {
        super(cause);
    }

    public BankException(String message, Exception e) {
        super(message, e);
    }
}
