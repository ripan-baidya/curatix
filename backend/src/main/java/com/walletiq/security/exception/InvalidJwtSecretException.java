package com.walletiq.security.exception;

import com.walletiq.common.constant.ErrorCode;
import com.walletiq.common.exception.InfrastructureException;

/**
 * This exception is thrown when the JWT secret configuration
 * is invalid.
 * <p>This exception is deprecated and should be removed when
 * the migration to RSA is complete.
 */
@Deprecated(since = "1.0", forRemoval = true)
public class InvalidJwtSecretException extends InfrastructureException {

    public InvalidJwtSecretException(ErrorCode errorCode) {
        super(errorCode);
    }

    public InvalidJwtSecretException(ErrorCode errorCode, String errorMessage) {
        super(errorCode, errorMessage);
    }

    public InvalidJwtSecretException(ErrorCode errorCode, String message, Throwable cause) {
        super(errorCode, message, cause);
    }
}
