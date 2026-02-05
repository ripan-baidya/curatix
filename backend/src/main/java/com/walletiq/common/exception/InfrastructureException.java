package com.walletiq.common.exception;

import com.walletiq.common.constant.ErrorCode;

/**
 * Base exception for infrastructure-related failures.
 * <p>These are typically unrecoverable system errors.
 */
public abstract class InfrastructureException extends CuratixException {

    protected InfrastructureException(ErrorCode errorCode) {
        super(errorCode);
    }

    protected InfrastructureException(ErrorCode errorCode, String message) {
        super(errorCode, message);
    }

    protected InfrastructureException(ErrorCode errorCode, String message, Throwable cause) {
        super(errorCode, message, cause);
    }

    protected InfrastructureException(ErrorCode errorCode, Throwable cause) {
        super(errorCode, cause);
    }
}