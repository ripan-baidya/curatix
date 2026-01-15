package com.curatix.security.exception;

import com.curatix.common.constant.ErrorCode;
import com.curatix.common.exception.AuthenticationException;

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
