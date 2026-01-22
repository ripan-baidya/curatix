package com.curatix.domain.identity.dto.command;

/**
 * Command to log in a user with email and password.
 *
 * @param email    User's email address.
 * @param password User's password.
 */
public record LoginCommand(
        String email,
        String password
) {
}
