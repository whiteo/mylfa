package de.whiteo.mylfa.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * @author Leo Tanas (<a href="https://github.com/whiteo">github</a>)
 */

@Getter
public class ExecutionConflictException extends RuntimeException {

    private final ErrorType errorType;
    private final HttpStatus status;

    public ExecutionConflictException(String message) {
        this(ErrorType.CONFLICT, HttpStatus.CONFLICT, message, null);
    }

    public ExecutionConflictException(ErrorType errorType, HttpStatus status, String message, Throwable error) {
        super(message, error);
        this.errorType = errorType;
        this.status = status;
    }
}