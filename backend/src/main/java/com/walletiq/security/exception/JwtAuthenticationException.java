package com.walletiq.security.exception;

import com.walletiq.common.constant.ErrorCode;
import com.walletiq.common.exception.AuthenticationException;

/**
 * This exception is thrown when there's an error related to JWT token.
 */
public class JwtAuthenticationException extends AuthenticationException {

    public JwtAuthenticationException(ErrorCode errorCode) {
        super(errorCode);
    }

    public JwtAuthenticationException(ErrorCode errorCode, String customMessage) {
        super(errorCode, customMessage);
    }
}
