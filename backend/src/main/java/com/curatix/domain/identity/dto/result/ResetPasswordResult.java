package com.curatix.domain.identity.dto.result;

/**
 * Response returned after a successful password reset.
 *
 * @param message Confirmation message indicating the result of
 *                the password reset operation.
 */
public record ResetPasswordResult(
        String message

) {
}
