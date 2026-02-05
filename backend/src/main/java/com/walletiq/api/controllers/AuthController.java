package com.walletiq.api.controllers;

import com.walletiq.api.annotation.PublicEndpoint;
import com.walletiq.api.dto.mapper.auth.AuthMapper;
import com.walletiq.api.dto.auth.LoginRequest;
import com.walletiq.api.dto.auth.RegisterAccountRequest;
import com.walletiq.api.dto.auth.LoginResponse;
import com.walletiq.api.dto.auth.PasswordRequirementResponse;
import com.walletiq.api.dto.auth.RegisterAccountResponse;
import com.walletiq.common.response.ErrorResponse;
import com.walletiq.common.response.ResponseWrapper;
import com.walletiq.common.util.ResponseUtil;
import com.walletiq.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/auth", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "authentication")
@Validated
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    @PublicEndpoint
    @Operation(summary = "User login")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Login successful"),
                    @ApiResponse(responseCode = "400", description = "Invalid login request"),
            }
    )
    public ResponseEntity<@NonNull ResponseWrapper<LoginResponse>> login(
            @Valid
            @RequestBody LoginRequest request
    ) {
        var command = AuthMapper.mapToLoginCommand(request);
        var result = authService.login(command);
        var response = AuthMapper.mapToLoginResponse(result);
        return ResponseUtil.ok("Login successful", response);
    }

    @PostMapping("/register")
    @PublicEndpoint
    @Operation(summary = "Register account")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Register successful"),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Invalid register request",
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
                    ),
            }
    )
    public ResponseEntity<@NonNull ResponseWrapper<RegisterAccountResponse>> register(
            @Valid
            @RequestBody RegisterAccountRequest request
    ) {
        System.out.println("RegisterAccountRequest: " + request);
        var command = AuthMapper.mapToRegisterAccountCommand(request);
        var result = authService.register(command);
        var response = AuthMapper.mapToRegisterAccountResponse(result);
        return ResponseUtil.ok("Account Registered successfully", response);
    }

    @GetMapping("/password-requirement")
    @PublicEndpoint
    @Operation(summary = "Get password requirements")
    @ApiResponse(responseCode = "200", description = "Password requirements fetched successfully")
    public ResponseEntity<@NonNull ResponseWrapper<PasswordRequirementResponse>> getPasswordRequirements() {
        var result = authService.getPasswordRequirements();
        var response = AuthMapper.mapToPasswordRequirementResponse(result);
        return ResponseUtil.ok("Password requirements fetched successfully", response);
    }
}
