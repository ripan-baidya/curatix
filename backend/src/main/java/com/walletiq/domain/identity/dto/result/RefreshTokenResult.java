package com.walletiq.domain.identity.dto.result;

/**
 * Response returned after refreshing tokens.
 */
public record RefreshTokenResult(
        TokenResult token
) {
}