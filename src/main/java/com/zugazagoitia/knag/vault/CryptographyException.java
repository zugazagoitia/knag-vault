package com.zugazagoitia.knag.vault;

public class CryptographyException extends Exception {
    public CryptographyException() {}

    public CryptographyException(String message) {
        super(message);
    }

    public CryptographyException(String message, Throwable cause) {
        super(message, cause);
    }

    public CryptographyException(Throwable cause) {
        super(cause);
    }
}
