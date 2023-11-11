package de.whiteo.mylfa.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * @author Leo Tanas (<a href="https://github.com/whiteo">github</a>)
 */

@Getter
public class NotFoundObjectException extends RuntimeException {

    private final ErrorType errorType;
    private final HttpStatus status;

    public NotFoundObjectException(String message) {
        this(ErrorType.NOT_FOUND, HttpStatus.NOT_FOUND, message, null);
    }

    public NotFoundObjectException(ErrorType errorType, HttpStatus status, String message, Throwable error) {
        super(message, error);
        this.errorType = errorType;
        this.status = status;
    }
}