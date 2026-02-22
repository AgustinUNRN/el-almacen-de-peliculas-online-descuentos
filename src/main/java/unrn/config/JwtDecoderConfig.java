package unrn.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtValidators;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

@Configuration
public class JwtDecoderConfig {

    private static final Logger log = LoggerFactory.getLogger(JwtDecoderConfig.class);

    @Bean
    public JwtDecoder jwtDecoder(
            @Value("${security.jwt.jwk-set-uri}") String jwkSetUri,
            @Value("${security.jwt.expected-issuer}") String expectedIssuer) {

        if (log.isDebugEnabled()) {
            log.debug("Configuring custom JwtDecoder with jwk-set-uri={} expected-issuer={}", jwkSetUri,
                    expectedIssuer);
        }

        NimbusJwtDecoder decoder = NimbusJwtDecoder.withJwkSetUri(jwkSetUri).build();

        OAuth2TokenValidator<Jwt> withIssuer = JwtValidators.createDefaultWithIssuer(expectedIssuer);
        decoder.setJwtValidator(withIssuer);

        return decoder;
    }

    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtAuthenticationConverter converter = new JwtAuthenticationConverter();

        Converter<Jwt, Collection<GrantedAuthority>> grantedAuthoritiesConverter = jwt -> {
            Collection<GrantedAuthority> authorities = new ArrayList<>();

            Object realmAccess = jwt.getClaim("realm_access");
            if (realmAccess instanceof Map) {
                Object roles = ((Map<?, ?>) realmAccess).get("roles");
                if (roles instanceof List) {
                    for (Object r : (List<?>) roles) {
                        if (r != null) {
                            authorities.add(new SimpleGrantedAuthority("ROLE_" + r.toString()));
                        }
                    }
                }
            }

            return authorities;
        };

        converter.setJwtGrantedAuthoritiesConverter(grantedAuthoritiesConverter);
        return converter;
    }
}
