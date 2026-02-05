package com.walletiq.domain.identity.dto.command;

/**
 * Command to refresh authentication tokens using a refresh token.
 *
 * @param refreshToken JWT refresh token for obtaining new access tokens.
 */
public record RefreshTokenCommand(
        String refreshToken
) {
}
