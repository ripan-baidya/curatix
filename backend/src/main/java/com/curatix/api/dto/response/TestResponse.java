package com.curatix.api.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Test API response dto
 */
@Schema(description = "Test API response")
public record TestResponse(
        String id,
        String name,
        String email
) {
    public static TestResponse of(String id, String name, String email) {
        return new TestResponse(id, name, email);
    }
}
