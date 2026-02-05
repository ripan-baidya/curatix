package com.walletiq.api.dto.mapper.auth;

import com.walletiq.api.dto.auth.*;
import com.walletiq.api.dto.auth.PasswordRequirementResponse;
import com.walletiq.api.dto.user.UserResponse;
import com.walletiq.domain.identity.dto.command.LoginCommand;
import com.walletiq.domain.identity.dto.command.RegisterAccountCommand;
import com.walletiq.domain.identity.dto.result.*;

/**
 * Utility class for mapping between domain and dto objects.
 */
public final class AuthMapper {

    private AuthMapper() {
    }

    /* Request Mapper */

    /**
     * Map {@link LoginRequest} to {@link LoginCommand}
     */
    public static LoginCommand mapToLoginCommand(LoginRequest request) {
        return new LoginCommand(
                request.email(),
                request.password()
        );
    }

    /**
     * Map {@link RegisterAccountRequest} to {@link RegisterAccountCommand}
     */
    public static RegisterAccountCommand mapToRegisterAccountCommand(RegisterAccountRequest request) {
        return new RegisterAccountCommand(
                request.fullName(),
                request.email(),
                request.password()
        );
    }

    /* Response Mapper */

    /**
     * Map {@link LoginResult} to {@link LoginResponse}
     */
    public static LoginResponse mapToLoginResponse(LoginResult result) {
        return new LoginResponse(
                mapToTokenResponse(result.token()),
                mapToUserResponse(result.user())
        );
    }

    /**
     * Map {@link RegisterAccountResult} to {@link RegisterAccountResponse}
     */
    public static RegisterAccountResponse mapToRegisterAccountResponse(RegisterAccountResult result) {
        return new RegisterAccountResponse(
                mapToTokenResponse(result.token()),
                mapToUserResponse(result.user())
        );
    }

    /**
     * Map PasswordRequirementResult to PasswordRequirementResponse
     */
    public static PasswordRequirementResponse mapToPasswordRequirementResponse(PasswordRequirementResult result) {
        return new PasswordRequirementResponse(
                result.minLength(),
                result.maxLength(),
                result.requireUppercase(),
                result.requireLowercase(),
                result.requireNumber(),
                result.description(),
                result.example()
        );
    }

    /* Helper */

    /**
     * Map {@link TokenResult} to {@link TokenResponse}
     */
    private static TokenResponse mapToTokenResponse(TokenResult result) {
        return new TokenResponse(
                result.accessToken(),
                result.refreshToken(),
                result.tokenType(),
                result.expiresIn()
        );
    }

    /**
     * Map {@link UserResult} to {@link UserResponse}
     */
    private static UserResponse mapToUserResponse(UserResult result) {
        return new UserResponse(
                result.id(),
                result.fullName(),
                result.email()
        );
    }
}
