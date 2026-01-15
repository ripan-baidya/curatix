package com.curatix.security.exception;

import com.curatix.common.constant.ErrorCode;
import com.curatix.common.exception.AuthenticationException;

/**
 * This exception occurs when the provided secret key is Invalid.
 */
public class InvalidSecretKeyException extends AuthenticationException {

    public InvalidSecretKeyException(ErrorCode errorCode) {
        super(errorCode);
    }
}
