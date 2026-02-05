package com.walletiq.api.dto.auth;

import com.walletiq.api.dto.user.UserResponse;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Register account response")
public record RegisterAccountResponse(
        TokenResponse token,
        UserResponse user
) {
}
