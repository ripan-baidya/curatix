package com.curatix.common.exception;

import com.curatix.common.constant.ErrorCode;
import com.curatix.common.response.FieldError;
import lombok.Getter;

import java.util.List;

/**
 * Exception for business logic violations, Use when operation violates
 * business rules {@code (422)}.
 */
@Getter
public class BusinessLogicException extends CuratixException {

    private final List<FieldError> fieldErrors;

    public BusinessLogicException(ErrorCode errorCode, List<FieldError> fieldErrors) {
        super(errorCode);
        this.fieldErrors = fieldErrors;
    }

    public BusinessLogicException(ErrorCode errorCode, String customMessage,
                                  List<FieldError> fieldErrors
    ) {
        super(errorCode, customMessage);
        this.fieldErrors = fieldErrors;
    }

    public BusinessLogicException(ErrorCode errorCode) {
        this(errorCode, List.of());
    }

    public BusinessLogicException(ErrorCode errorCode, String customMessage) {
        this(errorCode, customMessage, List.of());
    }

}