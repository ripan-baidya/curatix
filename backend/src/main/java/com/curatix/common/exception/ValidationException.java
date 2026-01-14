package com.curatix.common.exception;

import com.curatix.common.constant.ErrorCode;
import com.curatix.common.response.FieldError;
import lombok.Getter;

import java.util.List;

/**
 * Exception for validation failures, Contains field-level errors for
 * detailed feedback.
 */
@Getter
public class ValidationException extends CuratixException {

    private final List<FieldError> fieldErrors;

    public ValidationException(ErrorCode errorCode, List<FieldError> fieldErrors) {
        super(errorCode);
        this.fieldErrors = fieldErrors;
    }

    public ValidationException(ErrorCode errorCode, String message, List<FieldError> fieldErrors) {
        super(errorCode, message);
        this.fieldErrors = fieldErrors;
    }

    public ValidationException(ErrorCode errorCode) {
        this(errorCode, List.of());
    }
}
