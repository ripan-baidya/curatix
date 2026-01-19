package com.curatix.config.security;

import com.curatix.security.authentication.JwtAuthenticationFilter;
import com.curatix.security.handler.CustomAccessDeniedHandler;
import com.curatix.security.handler.JwtAuthenticationEntryPoint;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

/**
 * Configuration for Spring Boot Web Security
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity(jsr250Enabled = true, securedEnabled = true, prePostEnabled = true)
@RequiredArgsConstructor
public class SecurityConfig {

    /**
     * Publicly accessible endpoints that bypass Spring Security authentication.
     * <p>Note: All API endpoints are prefixed with <code>/api/{version}</code>
     * (e.g. /api/v1, /api/v2). The version prefix is handled separately and
     * therefore omitted from these patterns.</p>
     */
    private static final String[] PUBLIC_ENDPOINTS = {
            "/test/**",
            "/auth/**",
            "/actuator/health/**", "/actuator/info",
            "/api-docs/**", "/v3/api-docs/**",
            "/swagger-ui/**", "/swagger-ui.html",
            "/error"
    };

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final CustomAccessDeniedHandler customAccessDeniedHandler;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    @Bean
    @Profile(value = "dev")
    public SecurityFilterChain securityFilterChain(HttpSecurity http) {
        http
                .cors(Customizer.withDefaults())
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(STATELESS))
                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                        .accessDeniedHandler(customAccessDeniedHandler)
                )
                .authorizeHttpRequests(request -> request
                        .requestMatchers(PUBLIC_ENDPOINTS).permitAll()
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .headers(header -> header
                        // Disable legacy XSS protection (modern browsers ignore it anyway)
                        .xssProtection(HeadersConfigurer.XXssConfig::disable)
                        // Relaxed Content Security Policy for development
                        .contentSecurityPolicy(csp -> csp
                                .policyDirectives(
                                        "default-src * data: blob: 'unsafe-inline' 'unsafe-eval'; " +
                                                "script-src * data: blob: 'unsafe-inline' 'unsafe-eval'; " +
                                                "style-src * 'unsafe-inline'; " +
                                                "img-src * data: blob:; " +
                                                "font-src * data:; " +
                                                "connect-src * ws: wss:; " +   // WebSocket / Vite / HMR
                                                "frame-ancestors *;"           // Allow iframes (Swagger, tools)
                                )
                        )
                        // Disable frame options
                        .frameOptions(HeadersConfigurer.FrameOptionsConfig::disable)
                );

        return http.build();
    }
}
