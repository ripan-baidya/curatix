package com.curatix.api.controllers;

import com.curatix.api.annotation.PublicEndpoint;
import com.curatix.api.dto.mapper.auth.AuthMapper;
import com.curatix.api.dto.response.auth.PasswordRequirementResponse;
import com.curatix.common.response.ResponseWrapper;
import com.curatix.common.util.ResponseUtil;
import com.curatix.domain.identity.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/auth", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "authentication")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @GetMapping("/password-requirement")
    @PublicEndpoint
    @Operation(summary = "Get password requirements")
    @ApiResponse(responseCode = "200", description = "Password requirements fetched successfully")
    public ResponseEntity<@NonNull ResponseWrapper<PasswordRequirementResponse>> getPasswordRequirements() {
        var result = authService.getPasswordRequirements();
        var response = AuthMapper.mapToPasswordRequirementResponse(result);
        return ResponseUtil.ok("Password requirements fetched successfully",response);
    }
}
