package com.walletiq.common.handler;

import com.walletiq.common.constant.ErrorCode;
import com.walletiq.common.exception.CuratixException;
import com.walletiq.common.exception.InfrastructureException;
import com.walletiq.common.exception.ResourceNotFoundException;
import com.walletiq.common.exception.ValidationException;
import com.walletiq.common.response.ErrorDetail;
import com.walletiq.common.response.ErrorResponse;
import com.walletiq.common.response.FieldError;
import com.walletiq.common.util.RequestContextUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

/**
 * Global exception handler for handling exceptions in the application.
 * <P>Converts exceptions into standardized error responses with specific
 * error codes and messages.</P>
 *
 * @since 1.0.0
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    /**
     * Handle all custom base exception
     */
    @ExceptionHandler(CuratixException.class)
    public ResponseEntity<@NonNull ErrorResponse> handleBaseException(
            CuratixException ex, HttpServletRequest request
    ) {
        ErrorCode errorCode = ex.getErrorCode();

        ErrorDetail errorDetail = ErrorDetail.builder()
                .type(errorCode.getErrorType())
                .code(errorCode.getErrorCode())
                .detail(ex.getCustomMessage())
                .path(request.getRequestURI())
                .build();
        return ResponseEntity
                .status(errorCode.getErrorType().getStatusCode())
                .body(ErrorResponse.of(errorDetail));
    }

    /**
     * Handles all other unexpected exceptions
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<@NonNull ErrorResponse> handleGeneralException(
            Exception ex, HttpServletRequest request
    ) {
        String traceId = RequestContextUtil.generateTraceId();
        log.error("Unexpected error [traceId={}]: ", traceId, ex);

        ErrorDetail errorDetail = ErrorDetail.builder()
                .type(ErrorCode.INTERNAL_ERROR.getErrorType())
                .code(ErrorCode.INTERNAL_ERROR.getErrorCode())
                .detail(ErrorCode.INTERNAL_ERROR.getDefaultMessage())
                .path(request.getRequestURI())
                .traceId(traceId)
                .build();
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ErrorResponse.of(errorDetail));
    }

    /**
     * Handles validation exceptions with field-level errors
     */
    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(
            ValidationException ex, HttpServletRequest request) {

        ErrorCode errorCode = ex.getErrorCode();

        log.warn("Validation error [{}]: {} - {} field errors",
                errorCode.getErrorCode(), ex.getCustomMessage(), ex.getFieldErrors().size());

        ErrorDetail errorDetail = ErrorDetail.builder()
                .type(errorCode.getErrorType())
                .code(errorCode.getErrorCode())
                .detail(ex.getCustomMessage())
                .path(request.getRequestURI())
                .errors(ex.getFieldErrors())
                .build();

        return ResponseEntity
                .status(errorCode.getErrorType().getStatusCode())
                .body(ErrorResponse.of(errorDetail));
    }

    /**
     * Handles Spring's @Valid validation failures
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex, HttpServletRequest request) {

        log.warn("Method argument validation failed - {} errors",
                ex.getBindingResult().getErrorCount());

        List<FieldError> fieldErrors = extractFieldErrors(ex.getBindingResult());

        ErrorDetail errorDetail = ErrorDetail.builder()
                .type(ErrorCode.VALIDATION_FAILED.getErrorType())
                .code(ErrorCode.VALIDATION_FAILED.getErrorCode())
                .detail(ErrorCode.VALIDATION_FAILED.getDefaultMessage())
                .path(request.getRequestURI())
                .errors(fieldErrors)
                .build();

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ErrorResponse.of(errorDetail));
    }

    /**
     * Handles resource not found exceptions
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFound(
            ResourceNotFoundException ex, HttpServletRequest request) {

        ErrorCode errorCode = ex.getErrorCode();

        log.warn("Resource not found [{}]: {}", errorCode.getErrorCode(), ex.getCustomMessage());

        ErrorDetail errorDetail = ErrorDetail.builder()
                .type(errorCode.getErrorType())
                .code(errorCode.getErrorCode())
                .detail(ex.getCustomMessage())
                .path(request.getRequestURI())
                .build();

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(ErrorResponse.of(errorDetail));
    }

    /**
     * Handles infrastructure exceptions
     */
    @ExceptionHandler(InfrastructureException.class)
    public ResponseEntity<ErrorResponse> handleInfrastructureException(
            InfrastructureException ex, HttpServletRequest request
    ) {
        ErrorCode errorCode = ex.getErrorCode();

        log.error("Infrastructure failure [{}]: {}", errorCode.getErrorCode(), ex.getCustomMessage());

        ErrorDetail errorDetail = ErrorDetail.builder()
                .type(errorCode.getErrorType())
                .code(errorCode.getErrorCode())
                .detail(ex.getCustomMessage())
                .path(request.getRequestURI())
                .build();

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ErrorResponse.of(errorDetail));
    }

    /**
     * Extract field errors from Spring's {@link BindingResult}
     */
    private List<FieldError> extractFieldErrors(BindingResult bindingResult) {
        return bindingResult.getFieldErrors().stream()
                .map(error -> new FieldError(
                        error.getField(),
                        error.getDefaultMessage(),
                        error.getRejectedValue(),
                        error.getCode()
                )).toList();
    }
}
