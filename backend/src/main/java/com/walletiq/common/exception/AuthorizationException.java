package com.walletiq.common.exception;

import com.walletiq.common.constant.ErrorCode;

/**
 * Exception for authorization failures, Use for {@code 403} errors
 * (insufficient permissions, access denied)
 */
public class AuthorizationException extends CuratixException {

    public AuthorizationException(ErrorCode errorCode) {
        super(errorCode);
    }

    public AuthorizationException(ErrorCode errorCode, String customMessage) {
        super(errorCode, customMessage);
    }
}
