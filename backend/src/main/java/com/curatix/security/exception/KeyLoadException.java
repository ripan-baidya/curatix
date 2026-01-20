package com.curatix.security.exception;

import com.curatix.common.constant.ErrorCode;
import com.curatix.common.exception.InfrastructureException;

/**
 * Exception thrown when RSA key loading fails during application
 * startup.
 * <p>{@code 500 Internal Server Error} will be returned.</p>
 */
public class KeyLoadException extends InfrastructureException {

    public KeyLoadException(ErrorCode errorCode) {
        super(errorCode);
    }

    public KeyLoadException(ErrorCode errorCode, String message) {
        super(errorCode, message);
    }

    public KeyLoadException(ErrorCode errorCode, String message, Throwable cause) {
        super(errorCode, message, cause);
    }
}
