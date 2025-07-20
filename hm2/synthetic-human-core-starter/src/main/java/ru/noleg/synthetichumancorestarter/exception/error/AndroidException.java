package ru.noleg.synthetichumancorestarter.exception.error;

import java.util.Map;

public class AndroidException extends RuntimeException {
    private final ErrorCode errorCode;
    private final Map<String, String> details;

    public AndroidException(ErrorCode errorCode, String message, Map<String, String> details) {
        super(message);
        this.errorCode = errorCode;
        this.details = details;
    }

    public AndroidException(ErrorCode errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
        this.details = null;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }

    public Map<String, String> getDetails() {
        return details;
    }
}