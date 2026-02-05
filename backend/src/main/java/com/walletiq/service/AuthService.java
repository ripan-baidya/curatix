package com.walletiq.service;

import com.walletiq.domain.identity.dto.command.ConfirmResetPasswordCommand;
import com.walletiq.domain.identity.dto.command.LoginCommand;
import com.walletiq.domain.identity.dto.command.RefreshTokenCommand;
import com.walletiq.domain.identity.dto.command.RegisterAccountCommand;
import com.walletiq.domain.identity.dto.result.*;

/**
 * Service interface for authentication-related operations.
 */
public interface AuthService {

    /**
     * Fetch password policy and validation requirements.
     */
    PasswordRequirementResult getPasswordRequirements();

    /**
     * Login user
     */
    LoginResult login(LoginCommand command);

    /**
     * Register new account
     */
    RegisterAccountResult register(RegisterAccountCommand command);

    /**
     * Refresh token
     */
    RefreshTokenResult refreshToken(RefreshTokenCommand command);

    /**
     * Reset password when user forgets his or her own password. An email will
     * be sent to the user's account with a password reset link. The user can
     * click on the link to create a new password.
     */
    ResetPasswordResult resetPassword(String email);

    /**
     * After user clicks on the password reset link in the email, this method
     * is called to confirm the password reset with the token from the link and
     * the new password provided by the user.
     */
    ConfirmResetPasswordResult confirmResetPassword(ConfirmResetPasswordCommand command);
}
