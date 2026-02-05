package com.walletiq.common.exception;

import com.walletiq.common.constant.ErrorCode;
import lombok.Getter;

/**
 * Base exception for all custom exceptions in {@code Curatix} application.
 * All domain-specific exceptions should extend this class.
 * <p>Uses {@link ErrorCode} enum to ensure consistent error handling across
 * the system.
 */
@Getter
public class CuratixException extends RuntimeException {

    private final ErrorCode errorCode;
    private final String customMessage;

    /**
     * Creates exception with error code and default message
     */
    public CuratixException(ErrorCode errorCode) {
        super(errorCode.getDefaultMessage());
        this.errorCode = errorCode;
        this.customMessage = errorCode.getDefaultMessage();
    }

    /**
     * Creates exception with error code and custom message
     */
    public CuratixException(ErrorCode errorCode, String customMessage) {
        super(errorCode.getDefaultMessage());
        this.errorCode = errorCode;
        this.customMessage = customMessage;
    }

    /**
     * Creates exception with error code, custom message, and cause
     */
    public CuratixException(ErrorCode errorCode, String customMessage, Throwable cause) {
        super(customMessage, cause);
        this.errorCode = errorCode;
        this.customMessage = customMessage;
    }

    /**
     * Creates exception with error code and cause (uses default message)
     */
    public CuratixException(ErrorCode errorCode, Throwable cause) {
        super(errorCode.getDefaultMessage(), cause);
        this.errorCode = errorCode;
        this.customMessage = errorCode.getDefaultMessage();
    }
}
