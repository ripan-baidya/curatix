package com.curatix.common.constant;

import com.curatix.common.response.ErrorType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * Centralized catalog of system-specific error codes used across the
 * Curatix platform. Each error code is associated with an {@link ErrorType}
 * and a corresponding HTTP status to ensure consistent error handling
 * and API responses.
 * <p>Format: {@code DOMAIN.SPECIFIC_ERROR} (e.g., {@code USER.NOT_FOUND},
 * {@code AUTH.INVALID_TOKEN})</p>
 */
@Getter
@AllArgsConstructor
public enum ErrorCode {

    // 1xx

    /**
     * General Errors
     */
    RESOURCE_NOT_FOUND("RESOURCE.NOT_FOUND", ErrorType.RESOURCE_NOT_FOUND, "The requested resource was not found"),

    INVALID_REQUEST("GENERAL.INVALID_REQUEST", ErrorType.BAD_REQUEST, "The request is malformed or invalid"),

    INTERNAL_ERROR("GENERAL.INTERNAL_ERROR", ErrorType.INTERNAL_SERVER_ERROR, "An unexpected error occurred. Please try again later"),

    SERVICE_UNAVAILABLE("GENERAL.SERVICE_UNAVAILABLE", ErrorType.SERVICE_UNAVAILABLE, "The service is temporarily unavailable"),

    ;
    private final String errorCode;
    private final ErrorType errorType;
    private final String defaultMessage;
}
