package com.walletiq.domain.identity.dto.result;

/**
 * Response returned after confirming a password reset.
 *
 * @param message Confirmation message indicating the result of the
 *                password reset operation.
 */
public record ConfirmResetPasswordResult(
        String message
) {
}
