package com.walletiq.api.controllers;

import com.walletiq.api.annotation.PublicEndpoint;
import com.walletiq.api.dto.test.TestRequest;
import com.walletiq.api.dto.test.TestResponse;
import com.walletiq.common.constant.ErrorCode;
import com.walletiq.common.exception.*;
import com.walletiq.common.response.FieldError;
import com.walletiq.common.response.ResponseWrapper;
import com.walletiq.common.util.ResponseUtil;
import com.walletiq.security.exception.JwtAuthenticationException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.jspecify.annotations.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Test controller
 * <p>Contains test endpoints for validating all response types and
 * exception handling. It covers - success response (200, 201, 202, 204),
 * client errors(400, 401, 403, 404, 409, 422), server errors(500, 502, 503, 504),
 * and pagination, validation, security scenarios etc.</p>
 * <p><b>Note: </b>Don't Use this in Production</p>
 *
 */
@RestController
@RequestMapping(value = "/test", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "test")
public class TestController {

    /* Success Response */

    @GetMapping("/success")
    @PublicEndpoint
    @Operation(summary = "Test 200 Success Response")
    @ApiResponse(responseCode = "200", description = "OK")
    public ResponseEntity<@NonNull ResponseWrapper<TestResponse>> testSuccess() {
        var data = TestResponse.of(UUID.randomUUID().toString(), "John Doe", "john@examle.com");
        return ResponseUtil.ok("Success response test", data);
    }

    @PostMapping("/created")
    @PublicEndpoint
    @Operation(summary = "Test 201 Created response")
    @ApiResponse(responseCode = "201", description = "Created")
    public ResponseEntity<@NonNull ResponseWrapper<TestResponse>> testCreated(
            @Valid @RequestBody TestRequest request
    ) {
        var data = TestResponse.of(UUID.randomUUID().toString(), request.name(), request.email());
        return ResponseUtil.ok("Test Validation", data);
    }

    @PostMapping("/async")
    @PublicEndpoint
    @Operation(summary = "Test 202 Accepted response (async operation)")
    @ApiResponse(responseCode = "202", description = "Accepted")
    public ResponseEntity<@NonNull ResponseWrapper<Map<String, Object>>> testAsync() {
        String jobId = UUID.randomUUID().toString();
        return ResponseUtil.async(
                "Job created successfully",
                jobId,
                "PENDING",
                300,
                "/api/v1/test/job/" + jobId
        );
    }

    @DeleteMapping("/no-content")
    @PublicEndpoint
    @Operation(summary = "Test 204 No Content response")
    @ApiResponse(responseCode = "204", description = "No Content")
    public ResponseEntity<@NonNull Void> testNoContent() {
        return ResponseUtil.noContent();
    }

    /* Pagination */

    @GetMapping("/paginated")
    @PublicEndpoint
    @Operation(summary = "Test paginated response")
    @ApiResponse(responseCode = "200", description = "OK")
    public ResponseEntity<@NonNull ResponseWrapper<Map<String, Object>>> testPaginated(Pageable pageable) {
        List<TestResponse> content = List.of(
                new TestResponse("1", "User One", "user1@example.com"),
                new TestResponse("2", "User Two", "user2@example.com"),
                new TestResponse("3", "User Three", "user3@example.com")
        );

        Page<@NonNull TestResponse> page = new PageImpl<>(content, pageable, 100);
        return ResponseUtil.paginated("Data retrieved successfully", page);
    }

    @GetMapping("/paginated/empty")
    @PublicEndpoint
    @Operation(summary = "Test empty paginated response")
    @ApiResponse(responseCode = "200", description = "OK")
    public ResponseEntity<@NonNull ResponseWrapper<Map<String, Object>>> testEmptyPaginated() {
        return ResponseUtil.emptyCollection("No data found", 10);
    }

    @GetMapping("/paginated/filtered")
    @PublicEndpoint
    @Operation(summary = "Test paginated response with filters")
    @ApiResponse(responseCode = "200", description = "OK")
    public ResponseEntity<@NonNull ResponseWrapper<Map<String, Object>>> testFilteredPaginated(
            @RequestParam(required = false) String status,
            Pageable pageable
    ) {
        List<TestResponse> content =
                List.of(new TestResponse("1", "Active User", "active@example.com"));
        Page<@NonNull TestResponse> page = new PageImpl<>(content, pageable, 1);
        Map<String, Object> filters = new HashMap<>();
        filters.put("status", status != null ? status : "all");

        return ResponseUtil.paginatedWithFilters("Filtered data retrieved", page, filters);
    }

    /* Batch Operation */

    @PostMapping("/batch")
    @PublicEndpoint
    @Operation(summary = "Test batch operation response")
    @ApiResponse(responseCode = "200", description = "OK")
    public ResponseEntity<@NonNull ResponseWrapper<Map<String, Object>>> testBatch() {
        List<TestResponse> successful = List.of(
                new TestResponse("1", "Success One", "success1@example.com"),
                new TestResponse("2", "Success Two", "success2@example.com")
        );

        List<Map<String, Object>> failed = List.of(
                Map.of("id", "3", "name", "Failed User", "error", "Invalid email format")
        );

        return ResponseUtil.batch("Batch operation completed", 3, successful, failed);
    }

    /* Validation Error */

    @PostMapping("/validation")
    @PublicEndpoint
    @Operation(summary = "Validation Response", description = "Returns a validation response")
    @ApiResponse(responseCode = "200", description = "OK")
    public ResponseEntity<@NonNull ResponseWrapper<String>> testValidation(
            @Valid @RequestBody TestRequest request
    ) {
        return ResponseUtil.ok("Test Validation", request.toString());
    }

    @PostMapping("/validation/custom")
    @PublicEndpoint
    @Operation(summary = "Test custom validation exception")
    @ApiResponse(responseCode = "422", description = "Unprocessable Entity")
    public ResponseEntity<@NonNull ResponseWrapper<String>> testCustomValidation() {
        throw new ValidationException(
                ErrorCode.VALIDATION_FAILED,
                "Custom validation failed",
                List.of(
                        new FieldError("username", "Username already taken", "testuser", "DUPLICATE"),
                        new FieldError("age", "Must be at least 18", 15, "MIN_AGE")
                )
        );
    }

    /* Authentication Errors (401) */

    @GetMapping("/auth/expired-token")
    @PublicEndpoint
    @Operation(summary = "Test expired token error (401)")
    @ApiResponse(responseCode = "401", description = "Token expired")
    public ResponseEntity<@NonNull ResponseWrapper<String>> testExpiredToken() {
        throw new JwtAuthenticationException(
                ErrorCode.TOKEN_EXPIRED,
                "JWT token has expired"
        );
    }

    @GetMapping("/auth/invalid-token")
    @PublicEndpoint
    @Operation(summary = "Test invalid token error (401)")
    @ApiResponse(responseCode = "401", description = "Invalid token")
    public ResponseEntity<@NonNull ResponseWrapper<String>> testInvalidToken() {
        throw new JwtAuthenticationException(
                ErrorCode.TOKEN_INVALID,
                "JWT token is invalid"
        );
    }

    @GetMapping("/auth/invalid-credentials")
    @PublicEndpoint
    @Operation(summary = "Test invalid credentials error (401)")
    @ApiResponse(responseCode = "401", description = "Invalid credentials")
    public ResponseEntity<@NonNull ResponseWrapper<String>> testInvalidCredentials() {
        throw new AuthenticationException(ErrorCode.INVALID_CREDENTIALS);
    }

    /* Authorization Errors (403) */

    @GetMapping("/authz/access-denied")
    @PublicEndpoint
    @Operation(summary = "Test access denied error (403)")
    @ApiResponse(responseCode = "403", description = "Access Denied")
    public ResponseEntity<@NonNull ResponseWrapper<String>> testAccessDenied() {
        throw new AuthorizationException(ErrorCode.ACCESS_DENIED);
    }

    @GetMapping("/authz/insufficient-permissions")
    @PublicEndpoint
    @Operation(summary = "Test insufficient permissions (403)")
    @ApiResponse(responseCode = "403", description = "Insufficient permissions")
    public ResponseEntity<@NonNull ResponseWrapper<String>> testInsufficientPermissions() {
        throw new AuthorizationException(ErrorCode.INSUFFICIENT_PERMISSIONS);
    }

    @GetMapping("/authz/admin-only")
    @PublicEndpoint
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Test role-based access (requires ADMIN role)")
    @ApiResponse(responseCode = "200", description = "OK")
    public ResponseEntity<@NonNull ResponseWrapper<String>> testAdminOnly() {
        return ResponseUtil.ok("Admin access granted", "You have ADMIN role");
    }

    /* Resource Not Found (404) */

    @GetMapping("/not-found/resource")
    @PublicEndpoint
    @Operation(summary = "Test resource not found (404)")
    @ApiResponse(responseCode = "404", description = "Resource not found")
    public ResponseEntity<@NonNull ResponseWrapper<String>> testResourceNotFound() {
        throw new ResourceNotFoundException(ErrorCode.RESOURCE_NOT_FOUND, "Resource with ID '999' not found");
    }

    @GetMapping("/not-found/user/{id}")
    @PublicEndpoint
    @Operation(summary = "Test user not found (404)")
    @ApiResponse(responseCode = "404", description = "User not found")
    public ResponseEntity<@NonNull ResponseWrapper<String>> testUserNotFound(@PathVariable String id) {
        throw new ResourceNotFoundException(ErrorCode.USER_NOT_FOUND, id);
    }

    /* Conflict Errors (409) */

    @PostMapping("/conflict/duplicate-email")
    @PublicEndpoint
    @Operation(summary = "Test duplicate email conflict (409)")
    @ApiResponse(responseCode = "409", description = "User with email 'test@example.com' already exists")
    public ResponseEntity<@NonNull ResponseWrapper<String>> testDuplicateEmail() {
        throw new ResourceConflictException(
                ErrorCode.USER_ALREADY_EXISTS,
                "User with email 'test@example.com' already exists",
                List.of(new FieldError("email", "This email is already registered", "test@example.com"))
        );
    }

    /* Business Logic Errors (422) */

    @DeleteMapping("/business-logic/cannot-delete-self")
    @PublicEndpoint
    @Operation(summary = "Test cannot delete self (422)")
    @ApiResponse(responseCode = "422", description = "Cannot delete self")
    public ResponseEntity<@NonNull ResponseWrapper<String>> testCannotDeleteSelf() {
        throw new BusinessLogicException(ErrorCode.USER_CANNOT_DELETE_SELF);
    }


    /* Rate Limiting (429) */

    @GetMapping("/rate-limit/exceeded")
    @PublicEndpoint
    @Operation(summary = "Test rate limit exceeded (429)")
    @ApiResponse(responseCode = "429", description = "Rate limit exceeded")
    public ResponseEntity<@NonNull ResponseWrapper<String>> testRateLimitExceeded() {
        throw new RateLimitException(
                ErrorCode.RATE_LIMIT_EXCEEDED,
                "Too many requests. Please try again in 60 seconds",
                60
        );
    }

    /* SERVER ERRORS (500) */

    @GetMapping("/server-error/internal")
    @PublicEndpoint
    @Operation(summary = "Test internal server error (500)")
    @ApiResponse(responseCode = "500", description = "Internal server error")
    public ResponseEntity<@NonNull ResponseWrapper<String>> testInternalError() {
        throw new RuntimeException("Simulated internal server error");
    }

    @GetMapping("/server-error/null-pointer")
    @PublicEndpoint
    @Operation(summary = "Test null pointer exception (500)")
    @ApiResponse(responseCode = "500", description = "Null pointer exception")
    public ResponseEntity<@NonNull ResponseWrapper<String>> testNullPointer() {
        String value = null;
        return ResponseUtil.ok("This will fail", value);
    }

    /* Service Unavailable (503) */

    @GetMapping("/service-unavailable")
    @PublicEndpoint
    @Operation(summary = "Test service unavailable (503)")
    public ResponseEntity<@NonNull ResponseWrapper<String>> testServiceUnavailable() {
        throw new CuratixException(
                ErrorCode.SERVICE_UNAVAILABLE,
                "Database service is temporarily unavailable"
        );
    }
}

