package com.walletiq.domain.identity.dto.result;

/**
 * Password requirement result
 */
public record PasswordRequirementResult(
        int minLength,
        int maxLength,
        boolean requireUppercase,
        boolean requireLowercase,
        boolean requireNumber,
        String description,
        String example
        // More customizable fields can be added here, such as special characters
        // and other password policies. for simplicity, they are omitted here.
) {
}
