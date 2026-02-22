package unrn.config;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.oauth2.core.OAuth2TokenValidatorResult;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtValidators;

import java.time.Instant;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("JwtIssuerValidator Tests")
public class JwtIssuerValidatorTest {

    @Test
    @DisplayName("JwtIssuerValidator_expectedIssuer_valid")
    void JwtIssuerValidator_expectedIssuer_valid() {
        // Setup
        String expectedIssuer = "http://localhost:9090/realms/videoclub";
        var validator = JwtValidators.createDefaultWithIssuer(expectedIssuer);

        Instant now = Instant.now();
        Jwt jwt = new Jwt("token", now, now.plusSeconds(60), Map.of("alg", "none"), Map.of("iss", expectedIssuer));

        // Exercise
        OAuth2TokenValidatorResult result = validator.validate(jwt);

        // Verify
        assertFalse(result.hasErrors(), "Validator should accept token with expected issuer");
    }

    @Test
    @DisplayName("JwtIssuerValidator_wrongIssuer_invalid")
    void JwtIssuerValidator_wrongIssuer_invalid() {
        // Setup
        String expectedIssuer = "http://localhost:9090/realms/videoclub";
        var validator = JwtValidators.createDefaultWithIssuer(expectedIssuer);

        Instant now = Instant.now();
        Jwt jwt = new Jwt("token", now, now.plusSeconds(60), Map.of("alg", "none"),
                Map.of("iss", "http://malicious/realm"));

        // Exercise
        OAuth2TokenValidatorResult result = validator.validate(jwt);

        // Verify
        assertTrue(result.hasErrors(), "Validator should reject token with wrong issuer");
    }
}
