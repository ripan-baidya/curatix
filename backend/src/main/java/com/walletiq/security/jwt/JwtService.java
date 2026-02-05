package com.walletiq.security.jwt;

import com.walletiq.common.constant.ErrorCode;
import com.walletiq.config.properties.JwtProperties;
import com.walletiq.security.exception.JwtAuthenticationException;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.SignatureException;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.time.Instant;
import java.util.Date;
import java.util.Map;
import java.util.function.Function;

@Service
@Slf4j
@RequiredArgsConstructor
public class JwtService {

    private static final String CLAIM_USER_ID = "uid";
    private static final String CLAIM_TOKEN_TYPE = "token_type";
    private static final String TOKEN_TYPE_ACCESS = "access";
    private static final String TOKEN_TYPE_REFRESH = "refresh";
    private static final String BEARER_PREFIX = "Bearer ";

    private final JwtProperties jwtProperties;
    private final ResourceLoader resourceLoader;
    private PrivateKey privateKey;
    private PublicKey publicKey;


    /**
     * Initialize the Secret Key
     */
    @PostConstruct
    private void initSecretKey() {
        log.info("Initializing RSA key pair for JWT authentication");

        try {
            // Load private key
            String privateKeyPath = jwtProperties.rsa().privateKeyPath();
            if (StringUtils.isBlank(privateKeyPath)) {
                throw new IllegalArgumentException("Private key path not configured");
            }
            this.privateKey = KeyUtils.loadPrivateKey(privateKeyPath, resourceLoader);

            // Load public key
            String publicKeyPath = jwtProperties.rsa().publicKeyPath();
            if (StringUtils.isBlank(publicKeyPath)) {
                throw new IllegalArgumentException("Public key path not configured");
            }
            this.publicKey = KeyUtils.loadPublicKey(publicKeyPath, resourceLoader);

            log.info("RSA key pair initialized successfully");
        } catch (Exception ex) {
            log.error("Failed to initialize RSA keys: {}", ex.getMessage(), ex);
            throw new IllegalStateException("JWT Service initialization failed", ex);
        }
    }

    /* Token Generation */

    /**
     * Generate JWT Access token
     */
    public String generateAccessToken(String id, String email) {
        Map<String, Object> claims = Map.of(
                CLAIM_USER_ID, id,
                CLAIM_TOKEN_TYPE, TOKEN_TYPE_ACCESS
        );
        return buildToken(claims, email, getExpirationInSeconds(TOKEN_TYPE_ACCESS));
    }

    /**
     * Generate JWT Refresh token
     */
    public String generateRefreshToken(String id, String email) {
        Map<String, Object> claims = Map.of(
                CLAIM_USER_ID, id,
                CLAIM_TOKEN_TYPE, TOKEN_TYPE_REFRESH
        );
        return buildToken(claims, email, getExpirationInSeconds(TOKEN_TYPE_REFRESH));
    }

    /* Token Validation */

    /**
     * Validates JWT token (strict validation with exceptions).
     *
     * @param token JWT token (with or without Bearer prefix)
     * @throws JwtAuthenticationException if token is invalid/expired
     */
    public void validateToken(String token) {
        extractClaims(token);
    }

    /**
     * Validate JWT token
     */
    public boolean isTokenValid(String token) {
        String jwt = stripBearerPrefix(token);
        if (StringUtils.isBlank(jwt) || !jwt.contains(".")) {
            log.warn("JWT token format is Invalid!");
            return false;
        }

        try {
            Jwts.parser()
                    // .verifyWith(secretKey)
                    .verifyWith(publicKey)
                    .requireIssuer(jwtProperties.issuer())
                    .clockSkewSeconds(60) // Allow 60s for server clock differences
                    .build()
                    .parseSignedClaims(jwt)
                    .getPayload();
            return true;
        } catch (ExpiredJwtException e) {
            log.warn("JWT expired: {}. Suggested action: Trigger Refresh Token flow.", e.getMessage());
            return false;
        } catch (SignatureException e) {
            log.error("CRITICAL: JWT signature mismatch. Possible tampering attempt!");
            return false;
        } catch (MalformedJwtException | UnsupportedJwtException e) {
            log.error("Invalid JWT structure: {}", e.getMessage());
            return false;
        } catch (Exception e) {
            log.error("JWT validation failed for unknown reason: {}", e.getMessage());
            return false;
        }
    }

    /**
     * Check whether token is expired
     */
    public boolean isTokenExpired(String token) {
        return extractClaims(token).getExpiration()
                .before(new Date());
    }

    /* Claim Extraction */

    /**
     * Generic method to extract any claim from the token
     */
    public <T> T extractClaim(String token, Function<Claims, T> claimResolver) {
        return claimResolver.apply(extractClaims(token));
    }

    /**
     * Extract user ID from token
     */
    public String extractUserId(String token) {
        Claims claims = extractClaims(token);
        final String userId = claims.get(CLAIM_USER_ID, String.class);

        if (userId == null || StringUtils.isBlank(userId)) {
            log.error("Token missing required user ID claim: {}", CLAIM_USER_ID);
            throw new JwtAuthenticationException(
                    ErrorCode.TOKEN_INVALID, "Token missing required user ID claim"
            );

        }

        return userId;
    }

    /**
     * Extract email(username) from token
     */
    public String extractEmail(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    /**
     * Extract token type
     */
    public String extractTokenType(String token) {
        Claims claims = extractClaims(token);
        return claims.get(CLAIM_TOKEN_TYPE, String.class);
    }

    /* Helper functions */

    /**
     * Strips the "Bearer" prefix from the token if present.
     */
    private String stripBearerPrefix(String token) {
        if (token != null && token.startsWith(BEARER_PREFIX)) {
            return token.substring(BEARER_PREFIX.length()).trim();
        }
        return token;
    }

    /**
     * Get expiration time of JWT token (in seconds)
     *
     * @param type token type for which we want to get the expiration
     */
    private long getExpirationInSeconds(String type) {
        return switch (type) {
            case TOKEN_TYPE_ACCESS -> jwtProperties.accessToken().expiration().getSeconds();
            case TOKEN_TYPE_REFRESH -> jwtProperties.refreshToken().expiration().getSeconds();
            default -> throw new IllegalArgumentException("Invalid token type: " + type);
        };
    }

    /**
     * Build JWT token (Access + Refresh) from Claims.
     */
    private String buildToken(Map<String, Object> claims, String email, long expInSeconds) {
        Instant now = Instant.now();
        Instant expAt = now.plusSeconds(expInSeconds);

        return Jwts.builder()
                .claims(claims)
                .issuer(jwtProperties.issuer())
                .subject(email)
                .issuedAt(Date.from(now))
                .expiration(Date.from(expAt))
                // .signWith(secretKey, Jwts.SIG.HS512)
                .signWith(privateKey, Jwts.SIG.RS512)
                .compact();
    }

    /**
     * Extract all claims from the JWT token
     */
    private Claims extractClaims(String token) {
        try {
            String jwt = stripBearerPrefix(token);
            return Jwts.parser()
                    .verifyWith(publicKey)
                    .requireIssuer(jwtProperties.issuer())
                    .clockSkewSeconds(60) // Allow 60s for server clock differences
                    .build()
                    .parseSignedClaims(jwt)
                    .getPayload();
        } catch (ExpiredJwtException e) {
            log.debug("JWT token expired: {}", e.getMessage());
            throw new JwtAuthenticationException(ErrorCode.TOKEN_EXPIRED);
        } catch (JwtException e) {
            log.error("Failed to extract claims: {}", e.getMessage());
            throw new JwtAuthenticationException(ErrorCode.TOKEN_INVALID);
        } catch (Exception e) {
            log.error("Unexpected error parsing JWT: {}", e.getMessage());
            throw new JwtAuthenticationException(ErrorCode.INTERNAL_ERROR);
        }
    }

}
