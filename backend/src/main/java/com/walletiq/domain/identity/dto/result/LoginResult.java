package com.walletiq.domain.identity.dto.result;

/**
 * Result returned after successful login.
 */
public record LoginResult(
        TokenResult token,
        UserResult user
) {
}
