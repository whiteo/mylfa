package de.whiteo.mylfa.exception;

/**
 * @author Leo Tanas (<a href="https://github.com/whiteo">github</a>)
 */

public class AppRuntimeException extends RuntimeException {

    public AppRuntimeException(Throwable cause) {
        super(cause);
    }
}