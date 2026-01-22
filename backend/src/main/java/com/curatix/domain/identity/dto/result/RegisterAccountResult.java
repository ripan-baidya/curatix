package com.curatix.domain.identity.dto.result;

/**
 * Result returned after successful account registration.
 */
public record RegisterAccountResult(
        TokenResult token,
        UserResult user
) {
}
