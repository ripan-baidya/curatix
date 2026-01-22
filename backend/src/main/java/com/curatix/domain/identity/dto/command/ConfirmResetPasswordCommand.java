package com.curatix.domain.identity.dto.command;

/**
 * Command to confirm password reset with a token and new password.
 *
 * @param token           Password reset token.
 * @param newPassword     New password to set.
 * @param confirmPassword Confirmation of the new password.
 */
public record ConfirmResetPasswordCommand(
        String token,
        String newPassword,
        String confirmPassword
) {
}
