package ru.noleg.synthetichumancorestarter.exception.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.support.MethodArgumentNotValidException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.noleg.synthetichumancorestarter.exception.error.AndroidException;
import ru.noleg.synthetichumancorestarter.exception.error.ErrorCode;

import java.time.Instant;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@RestControllerAdvice
public class AndroidExceptionHandler {

    @ExceptionHandler(AndroidException.class)
    public ResponseEntity<ErrorResponse> handleAndroidException(AndroidException ex) {
        ErrorResponse response = new ErrorResponse(
                ex.getErrorCode(),
                ex.getMessage(),
                ex.getDetails(),
                Instant.now()
        );
        return ResponseEntity
                .status(resolveHttpStatus(ex.getErrorCode()))
                .body(response);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = Objects.requireNonNull(ex.getBindingResult()).getFieldErrors().stream()
                .collect(Collectors.toMap(
                        FieldError::getField,
                        fieldError -> fieldError.getDefaultMessage() != null ?
                                fieldError.getDefaultMessage() : "Validation error"
                ));

        ErrorResponse response = new ErrorResponse(
                ErrorCode.COMMAND_VALIDATION_ERROR,
                "Command validation failed",
                errors,
                Instant.now()
        );

        return ResponseEntity.badRequest().body(response);
    }

    private HttpStatus resolveHttpStatus(ErrorCode errorCode) {
        return switch (errorCode) {
            case COMMAND_VALIDATION_ERROR -> HttpStatus.BAD_REQUEST;
            case QUEUE_OVERFLOW -> HttpStatus.TOO_MANY_REQUESTS;
            case AUDIT_FAILURE, METRIC_PUBLISH_FAILURE -> HttpStatus.FAILED_DEPENDENCY;
            default -> HttpStatus.INTERNAL_SERVER_ERROR;
        };
    }

    public record ErrorResponse(ErrorCode code, String message, Map<String, String> details, Instant timestamp) {
    }
}