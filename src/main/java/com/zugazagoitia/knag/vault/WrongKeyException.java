package com.zugazagoitia.knag.vault;

public class WrongKeyException extends Exception {
    public WrongKeyException() {}

    public WrongKeyException(String message) {
        super(message);
    }

    public WrongKeyException(String message, Throwable cause) {
        super(message, cause);
    }

    public WrongKeyException(Throwable cause) {
        super(cause);
    }
}
