package com.curatix.domain.identity.dto.result;

/**
 * User dto contains basic user information.
 *
 * @param id       id of the user
 * @param fullName full name of the user
 * @param email    email of the user
 */
public record UserResult(
        String id,
        String fullName,
        String email
) {
    public static UserResult of(String id, String fullName, String email) {
        return new UserResult(id, fullName, email);
    }
}
