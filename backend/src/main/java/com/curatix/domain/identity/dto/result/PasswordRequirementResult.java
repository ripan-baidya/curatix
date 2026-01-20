package com.curatix.domain.identity.dto.result;

/**
 * Password requirement result
 */
public record PasswordRequirementResult(
        int minLength,
        int maxLength,
        boolean requireUppercase,
        boolean requireLowercase,
        boolean requireNumber,
        boolean requireSpecialCharacter,
        String allowedSpecialCharacters,
        String description,
        String example
) {
}
