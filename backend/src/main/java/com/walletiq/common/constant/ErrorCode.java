package com.walletiq.common.constant;

import com.walletiq.common.response.ErrorType;
import lombok.AllArgsConstructor;
import lombok.Getter;

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

    /*      */
    INVALID_SECRET_KEY("AUTH.INVALID_SECRET_KEY", ErrorType.AUTHENTICATION_ERROR, "Invalid Secret key"),

    TOKEN_EXPIRED("AUTH.TOKEN_EXPIRED", ErrorType.AUTHENTICATION_ERROR, "Authentication token has expired"),

    TOKEN_INVALID("AUTH.TOKEN_INVALID", ErrorType.AUTHENTICATION_ERROR, "Authentication token is invalid or malformed"),

    TOKEN_MISSING("AUTH.TOKEN_MISSING", ErrorType.AUTHENTICATION_ERROR, "Authentication token is required but not provided"),

    /*     */

    INVALID_CREDENTIALS("AUTH.INVALID_CREDENTIALS", ErrorType.AUTHENTICATION_ERROR, "Invalid email or password"),

    ACCESS_DENIED("AUTHZ.ACCESS_DENIED", ErrorType.AUTHORIZATION_ERROR, "You do not have permission to access this resource"),

    INSUFFICIENT_PERMISSIONS("AUTHZ.INSUFFICIENT_PERMISSIONS", ErrorType.AUTHORIZATION_ERROR, "You lack the required permissions to perform this action"),

    TENANT_ACCESS_DENIED("AUTHZ.TENANT_ACCESS_DENIED", ErrorType.AUTHORIZATION_ERROR, "You do not have access to this tenant"),

    ROLE_REQUIRED("AUTHZ.ROLE_REQUIRED", ErrorType.AUTHORIZATION_ERROR, "This action requires a specific role"),
    ROLE_NOT_FOUND("USERS.ROLE_NOT_FOUND", ErrorType.RESOURCE_NOT_FOUND, "This action requires a specific role"),

    /* VALIDATION ERRORS (5xxx) */
    VALIDATION_FAILED("VALIDATION.FAILED", ErrorType.VALIDATION_ERROR, "One or more fields have validation errors"),

    INVALID_EMAIL_FORMAT("VALIDATION.INVALID_EMAIL", ErrorType.VALIDATION_ERROR, "Email address format is invalid"),

    INVALID_DATE_RANGE("VALIDATION.INVALID_DATE_RANGE", ErrorType.VALIDATION_ERROR, "Start date must be before end date"),

    FIELD_REQUIRED("VALIDATION.FIELD_REQUIRED", ErrorType.VALIDATION_ERROR, "Required field is missing"),

    FIELD_TOO_LONG("VALIDATION.FIELD_TOO_LONG", ErrorType.VALIDATION_ERROR, "Field exceeds maximum allowed length"),

    INVALID_PASSWORD("VALIDATION.INVALID_PASSWORD", ErrorType.VALIDATION_ERROR, "Password is invalid"),
    INVALID_FORMAT("VALIDATION.INVALID_FORMAT", ErrorType.VALIDATION_ERROR, "Field format is invalid"),
    USER_NOT_FOUND("USER.NOT_FOUND", ErrorType.RESOURCE_NOT_FOUND, "User not found"),
    USER_ALREADY_EXISTS("USER.ALREADY_EXIST", ErrorType.RESOURCE_CONFLICT, "User already exists"),
    DUPLICATE_EMAIL("USER.DUPLICATE_EMAIL", ErrorType.RESOURCE_CONFLICT, "Email already in use"),
    USER_CANNOT_DELETE_SELF("USER.CANNOT_DELETE_SELF", ErrorType.BUSINESS_LOGIC_ERROR, "You cannot delete yourself"),
    RATE_LIMIT_EXCEEDED("RATE.LIMIT_EXCEEDED", ErrorType.RATE_LIMIT_ERROR, "Rate limit exceeded"),

    /* INFRASTRUCTURE ERRORS (6xxx) */
    KEY_FILE_NOT_FOUND("KEY_FILE_NOT_FOUND", ErrorType.INTERNAL_SERVER_ERROR, "Key file not found"),
    KEY_FILE_NOT_READABLE("KEY_FILE_NOT_READABLE", ErrorType.INTERNAL_SERVER_ERROR , "Key file is not readable" ),

    INVALID_KEY_FORMAT("INVALID_KEY_FORMAT", ErrorType.INTERNAL_SERVER_ERROR , "Invalid key format" ),

    PRIVATE_KEY_LOAD_FAILED("PRIVATE_KEY_LOAD_FAILED", ErrorType.INTERNAL_SERVER_ERROR, "Failed to load private key"),
    PUBLIC_KEY_LOAD_FAILED("PUBLIC_KEY_LOAD_FAILED", ErrorType.INTERNAL_SERVER_ERROR, "Failed to load public key"),

    ;
    private final String errorCode;
    private final ErrorType errorType;
    private final String defaultMessage;
}
