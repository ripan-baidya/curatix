package com.walletiq.api.dto.auth;

import com.walletiq.api.dto.user.UserResponse;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Login response")
public record LoginResponse(
        TokenResponse token,
        UserResponse user
) {
}
