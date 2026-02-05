package com.walletiq.domain.identity.dto.command;

/**
 * Register user account
 *
 * @param fullName full name of the user
 * @param email    email of the user
 * @param password password of the user
 */
public record RegisterAccountCommand(
        String fullName,
        String email,
        String password
) {
}
