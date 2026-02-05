package com.walletiq.api.dto.user;

/**
 * User dto contains basic user information.
 *
 * @param id       id of the user
 * @param fullName full name of the user
 * @param email    email of the user
 */
public record UserResponse(
        String id,
        String fullName,
        String email
) {
}
