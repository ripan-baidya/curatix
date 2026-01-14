package com.curatix.common.exception;

import com.curatix.common.constant.ErrorCode;

/**
 * Exception for resource not found scenarios, Use for {@code 404}
 * errors when a specific resource doesn't exist.
 */
public class ResourceNotFoundException extends CuratixException {

    public ResourceNotFoundException(ErrorCode errorCode) {
        super(errorCode);
    }

    public ResourceNotFoundException(ErrorCode errorCode, String customMessage) {
        super(errorCode, customMessage);
    }
}
