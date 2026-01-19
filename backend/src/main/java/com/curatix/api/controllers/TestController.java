package com.curatix.api.controllers;

import com.curatix.api.annotation.PublicEndpoint;
import com.curatix.api.dto.request.TestRequest;
import com.curatix.api.dto.response.TestResponse;
import com.curatix.common.constant.ErrorCode;
import com.curatix.common.exception.ResourceNotFoundException;
import com.curatix.common.response.ResponseWrapper;
import com.curatix.common.util.ApiResponseUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.jspecify.annotations.NonNull;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Test controller
 * <p>Contains test endpoints for API testing</p>
 */
@RestController
@RequestMapping(value = "/test", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "test")
public class TestController {

    @GetMapping("/success")
    @PublicEndpoint
    @Operation(summary = "Success Response", description = "Returns a success response")
    @ApiResponse(responseCode = "200", description = "OK")
    public ResponseEntity<@NonNull ResponseWrapper<String>> testSuccess() {
        return ApiResponseUtil.ok("Hello World");
    }

    @GetMapping("/not-found")
    @PublicEndpoint
    @Operation(summary = "Not Found Response", description = "Returns a not found response")
    @ApiResponse(responseCode = "404", description = "Not Found")
    public ResponseEntity<@NonNull ResponseEntity<@NonNull String>> testNotFound() {
        throw new ResourceNotFoundException(ErrorCode.RESOURCE_NOT_FOUND, "123");
    }

    @PostMapping("/validation")
    @PublicEndpoint
    @Operation(summary = "Validation Response", description = "Returns a validation response")
    @ApiResponse(responseCode = "200", description = "OK")
    public ResponseEntity<@NonNull ResponseWrapper<TestResponse>> testValidation(
            @Valid @RequestBody TestRequest request
    ) {
        return ApiResponseUtil.ok("Test Validation", TestResponse.of(request));
    }

}

