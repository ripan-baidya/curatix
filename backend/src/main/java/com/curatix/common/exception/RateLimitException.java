package com.curatix.common.exception;

import com.curatix.common.constant.ErrorCode;
import lombok.Getter;

/**
 * Exception for rate limit exceeded scenarios, Use for {@code 429}
 * errors when user exceeds API limits.
 */
@Getter
public class RateLimitException extends CuratixException {

    private final Integer retryAfter;

    public RateLimitException(ErrorCode errorCode, Integer retryAfter) {
        super(errorCode);
        this.retryAfter = retryAfter;
    }

    public RateLimitException(ErrorCode errorCode, String customMessage,
                              Integer retryAfter
    ) {
        super(errorCode, customMessage);
        this.retryAfter = retryAfter;
    }
}