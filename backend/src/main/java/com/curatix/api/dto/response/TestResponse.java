package com.curatix.api.dto.response;

import com.curatix.api.dto.request.TestRequest;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Test API response
 */
@Schema(description = "Test API response")
public record TestResponse(
        String name,
        String msg,
        Integer age,
        String email
) {
    public static TestResponse of(TestRequest request) {
        return new TestResponse(request.name(), request.msg(), request.age(), request.email());
    }
}
