package com.walletiq.common.exception;

import com.walletiq.common.constant.ErrorCode;
import com.walletiq.common.response.FieldError;
import lombok.Getter;

import java.util.List;

/**
 * Exception for resource conflicts (e.g., duplicates), Use for {@code 409}
 * errors when resource already exists.
 */
@Getter
public class ResourceConflictException extends CuratixException {

    private final List<FieldError> fieldErrors;

    public ResourceConflictException(ErrorCode errorCode, List<FieldError> fieldErrors) {
        super(errorCode);
        this.fieldErrors = fieldErrors;
    }

    public ResourceConflictException(ErrorCode errorCode, String customMessage,
                                     List<FieldError> fieldErrors
    ) {
        super(errorCode, customMessage);
        this.fieldErrors = fieldErrors;
    }

    public ResourceConflictException(ErrorCode errorCode) {
        this(errorCode, List.of());
    }
}