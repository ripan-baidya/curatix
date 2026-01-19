package com.curatix.api.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

/**
 * Test request
 */
public record TestRequest(

        @NotBlank(message = "Name must not be blank")
        @Size(min = 3, max = 50, message = "Name must be between 3 and 50 characters")
        @Schema(description = "User name", example = "John Doe")
        String name,

        @NotBlank(message = "Message must not be blank")
        @Size(min = 5, max = 200, message = "Message must be between 5 and 200 characters")
        @Schema(description = "Message content", example = "Hello from validation test")
        String msg,

        @NotNull(message = "Age is required")
        @Min(value = 18, message = "Age must be at least 18")
        @Max(value = 60, message = "Age must not exceed 60")
        @Schema(description = "User age", example = "25")
        Integer age,

        @Email(message = "Email must be valid")
        @NotBlank(message = "Email is required")
        @Schema(description = "User email address", example = "test@example.com")
        String email
) {
}
