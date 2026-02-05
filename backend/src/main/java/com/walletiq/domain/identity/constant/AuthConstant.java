package com.walletiq.domain.identity.constant;

/**
 * Constants related to authentication.
 */
public final class AuthConstant {

    private  AuthConstant() {
    }

    public static  final String ROLE_USER = "ROLE_USER";
    public static final String ROLE_ADMIN = "ROLE_ADMIN";

    public static final String PASSWORD_POLICY_MESSAGE = "Password must be at least 8-20 characters " +
            "contain at least one uppercase letter, one lowercase letter, " +
            "one digit";
    public static final String PASSWORD_POLICY_EXAMPLE = "SecurePass123";


}
