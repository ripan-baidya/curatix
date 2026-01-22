package com.curatix.domain.identity.dto.result;

/**
 * Response containing authentication tokens.
 *
 * @param accessToken  JWT access token for authenticated requests.
 * @param refreshToken JWT refresh token for obtaining new access tokens.
 * @param tokenType    Type of the token, typically "Bearer".
 * @param expiresIn    Expiration time of the access token in seconds.
 */
public record TokenResult(
        String accessToken,
        String refreshToken,
        String tokenType,
        Long expiresIn
) {
    public static TokenResult of(String accessToken, String refreshToken, String tokenType, Long expiresIn) {
        return new TokenResult(accessToken, refreshToken, tokenType, expiresIn);
    }
}
