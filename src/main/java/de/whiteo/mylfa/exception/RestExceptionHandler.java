package de.whiteo.mylfa.exception;

import jakarta.validation.ConstraintViolationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConversionException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.time.LocalDateTime;
import java.util.List;

import static de.whiteo.mylfa.exception.ExceptionHelper.getJsonExceptionMessage;
import static de.whiteo.mylfa.exception.ExceptionHelper.getRootCauseMessage;
import static java.util.Optional.ofNullable;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

/**
 * @author Leo Tanas (<a href="https://github.com/whiteo">github</a>)
 */

@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class RestExceptionHandler {

    public static ResponseEntity<ResponseError> buildResponse(HttpStatus status, ErrorType code, String msg,
                                                              Throwable ex) {
        if (NOT_FOUND.equals(status) || CONFLICT.equals(status)) {
            log.warn(msg);
        } else {
            log.error(msg, ex);
        }
        return ResponseEntity.status(status.value())
                .body(ResponseError.builder()
                        .code(code.name())
                        .message(msg)
                        .timestamp(LocalDateTime.now())
                        .status(status.value())
                        .build());
    }

    @ExceptionHandler(Throwable.class)
    @ResponseStatus(INTERNAL_SERVER_ERROR)
    public ResponseEntity<ResponseError> handleUndefinedException(Throwable ex) {
        String message = "Undefined error: " + getRootCauseMessage(ex);
        return buildResponse(INTERNAL_SERVER_ERROR, ErrorType.UNDEFINED_ERROR, message, ex);
    }

    @ExceptionHandler(HttpClientErrorException.class)
    @ResponseStatus(UNAUTHORIZED)
    protected ResponseEntity<ResponseError> handleHttpClientErrorException(HttpClientErrorException ex) {
        return buildResponse(UNAUTHORIZED, ErrorType.AUTHORIZATION_ERROR, ex.getMessage(), ex);
    }

    @ExceptionHandler(BadCredentialsException.class)
    @ResponseStatus(UNAUTHORIZED)
    protected ResponseEntity<ResponseError> handleBadCredentialsException(BadCredentialsException ex) {
        return buildResponse(UNAUTHORIZED, ErrorType.AUTHORIZATION_ERROR, ex.getMessage(), ex);
    }

    @ExceptionHandler(ExecutionConflictException.class)
    @ResponseStatus(CONFLICT)
    protected ResponseEntity<ResponseError> handleExecutionConflictException(ExecutionConflictException ex) {
        return buildResponse(CONFLICT, ErrorType.CONFLICT, ex.getMessage(), ex);
    }

    @ExceptionHandler(NotFoundObjectException.class)
    @ResponseStatus(NOT_FOUND)
    protected ResponseEntity<ResponseError> handleNotFoundObjectException(NotFoundObjectException ex) {
        return buildResponse(NOT_FOUND, ErrorType.NOT_FOUND, ex.getMessage(), ex);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseStatus(BAD_REQUEST)
    protected ResponseEntity<ResponseError> handleMethodArgumentTypeMismatchException(
            MethodArgumentTypeMismatchException ex) {
        return buildResponse(BAD_REQUEST, ErrorType.BAD_REQUEST, ex.getMessage(), ex);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(BAD_REQUEST)
    public ResponseEntity<ResponseError> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
        return buildResponse(BAD_REQUEST, ErrorType.BAD_REQUEST, ex.getMessage(), ex);
    }

    @ExceptionHandler(HttpMessageConversionException.class)
    @ResponseStatus(BAD_REQUEST)
    public ResponseEntity<ResponseError> handleMessageConversionException(HttpMessageConversionException ex) {
        return buildResponse(BAD_REQUEST, ErrorType.REQUEST_MESSAGE_CONVERSION_ERROR,
                getJsonExceptionMessage(ex), ex);
    }

    @ExceptionHandler(ServletRequestBindingException.class)
    @ResponseStatus(BAD_REQUEST)
    public ResponseEntity<ResponseError> handleServletRequestBinding(ServletRequestBindingException ex) {
        return buildResponse(BAD_REQUEST, ErrorType.REQUEST_MISSING_PARAMETER,
                getRootCauseMessage(ex), ex);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(BAD_REQUEST)
    public ResponseEntity<ResponseError> handleConstraintViolation(ConstraintViolationException ex) {
        return buildResponse(BAD_REQUEST, ErrorType.REQUEST_MISSING_PARAMETER,
                getRootCauseMessage(ex), ex);
    }

    @ExceptionHandler(TypeMismatchException.class)
    @ResponseStatus(BAD_REQUEST)
    public ResponseEntity<ResponseError> handleTypeMismatch(TypeMismatchException ex) {
        String requiredType = ofNullable(ex.getRequiredType()).map(Class::getSimpleName).orElse("<empty>");
        String message = getRootCauseMessage(ex) + ", required type '" + requiredType + "'";
        return buildResponse(BAD_REQUEST, ErrorType.REQUEST_TYPE_MISMATCH, message, ex);
    }

    @ExceptionHandler(UnsupportedOperationException.class)
    @ResponseStatus(BAD_REQUEST)
    public ResponseEntity<ResponseError> handleUnsupportedOperation(UnsupportedOperationException ex) {
        return buildResponse(BAD_REQUEST, ErrorType.UNSUPPORTED_OPERATION, getRootCauseMessage(ex), ex);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(BAD_REQUEST)
    @ResponseBody
    public ResponseEntity<ResponseError> validationError(MethodArgumentNotValidException ex) {
        BindingResult result = ex.getBindingResult();
        List<FieldError> fieldErrors = result.getFieldErrors();
        String errors = fieldErrors.stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage()).findFirst().orElse("ops!");
        return buildResponse(BAD_REQUEST, ErrorType.BAD_REQUEST, errors, ex);
    }
}