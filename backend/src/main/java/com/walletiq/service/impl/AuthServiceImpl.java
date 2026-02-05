package com.walletiq.service.impl;

import com.walletiq.config.properties.JwtProperties;
import com.walletiq.domain.identity.constant.AuthConstant;
import com.walletiq.domain.identity.dto.command.ConfirmResetPasswordCommand;
import com.walletiq.domain.identity.dto.command.LoginCommand;
import com.walletiq.domain.identity.dto.command.RefreshTokenCommand;
import com.walletiq.domain.identity.dto.command.RegisterAccountCommand;
import com.walletiq.domain.identity.dto.result.*;
import com.walletiq.entity.User;
import com.walletiq.repository.UserRepository;
import com.walletiq.service.AuthService;
import com.walletiq.validator.AuthValidator;
import com.walletiq.security.jwt.JwtService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementation of the {@link AuthService}
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;

    private final JwtService jwtService;
    private final JwtProperties jwtProperties;
    private final AuthValidator authValidator;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    @Override
    public PasswordRequirementResult getPasswordRequirements() {
        return new PasswordRequirementResult(
                8, 20, true, true, true,
                AuthConstant.PASSWORD_POLICY_MESSAGE,
                AuthConstant.PASSWORD_POLICY_EXAMPLE
        );
    }

    @Override
    public LoginResult login(LoginCommand command) {
        final Authentication auth = this.authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        command.email(),
                        command.password()
                )
        );

        final User user = (User) auth.getPrincipal();

        return new LoginResult(
                buildTokenResponse(user),
                buildUserResult(user)
        );
    }

    @Override
    @Transactional
    public RegisterAccountResult register(RegisterAccountCommand command) {
        log.debug("Registering new user with email: {}", command.email());

        var passwordRequirements = getPasswordRequirements();

        authValidator.ensureValidAndUniqueEmail(command.email());
        authValidator.validatePassword(command.password(), passwordRequirements);

        final User newUser = new User();
        newUser.updateFullName(command.fullName());
        newUser.updateEmail(command.email());
        newUser.updatePassword(passwordEncoder.encode(command.password()));

        User savedUser = this.userRepository.save(newUser);
        log.debug("New user registered with ID: {}", savedUser.getId());

        // build and return the result
        log.debug("Registration successful for user ID: {}", savedUser.getId());
        return new RegisterAccountResult(
                buildTokenResponse(savedUser),
                buildUserResult(savedUser)
        );
    }

    @Override
    public RefreshTokenResult refreshToken(RefreshTokenCommand command) {
        return null;
    }

    @Override
    public ResetPasswordResult resetPassword(String email) {
        return null;
    }

    @Override
    public ConfirmResetPasswordResult confirmResetPassword(ConfirmResetPasswordCommand command) {
        return null;
    }

    /* Private helper methods */

    /**
     * Build TokenResponse from User entity
     */
    private TokenResult buildTokenResponse(User user) {
        return TokenResult.of(
                jwtService.generateAccessToken(user.getId(), user.getEmail()),
                jwtService.generateRefreshToken(user.getId(), user.getEmail()),
                jwtProperties.prefix(),
                jwtProperties.accessToken().expiration().getSeconds()
        );
    }

    /**
     * Build UserResult from User entity
     */
    private UserResult buildUserResult(User user) {
        return UserResult.of(
                user.getId(),
                user.getFullName(),
                user.getEmail()
        );
    }
}
