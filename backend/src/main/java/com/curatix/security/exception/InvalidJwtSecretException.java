package com.curatix.security.exception;

import com.curatix.common.constant.ErrorCode;
import com.curatix.common.exception.AuthenticationException;

/**
 * This exception occurs when the provided secret key is Invalid.
 */
public class InvalidJwtSecretException extends AuthenticationException {

    public InvalidJwtSecretException(ErrorCode errorCode) {
        super(errorCode);
    }

    public InvalidJwtSecretException(ErrorCode errorCode, String errorMessage) {
        super(errorCode, errorMessage);
    }
}
