package com.walletiq.domain.identity.exception;

import com.walletiq.common.constant.ErrorCode;
import com.walletiq.common.exception.ResourceNotFoundException;

/**
 * Exception thrown for user-related errors.
 */
public class UserException extends ResourceNotFoundException {

    public UserException(ErrorCode errorCode) {
        super(errorCode);
    }

    public UserException(ErrorCode errorCode, String customMessage) {
        super(errorCode, customMessage);
    }
}
