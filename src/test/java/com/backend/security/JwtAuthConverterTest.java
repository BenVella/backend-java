package com.backend.security;

import java.util.List;
import java.util.Map;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;

import static org.assertj.core.api.Assertions.assertThat;

class JwtAuthConverterTest {

    private final KeycloakSecurityProperties properties = new KeycloakSecurityProperties();
    private final JwtAuthConverter converter = new JwtAuthConverter(properties);

    @ParameterizedTest
    @CsvSource({
            "user,ROLE_USER",
            "admin,ROLE_ADMIN",
            "support,ROLE_SUPPORT"
    })
    void convertsRealmRolesToSpringAuthorities(String sourceRole, String expectedAuthority) {
        Jwt jwt = Jwt.withTokenValue("token")
                .header("alg", "none")
                .claim("realm_access", Map.of("roles", List.of(sourceRole)))
                .build();

        Authentication authentication = converter.convert(jwt);

        assertThat(authentication.getAuthorities())
                .extracting("authority")
                .contains(expectedAuthority);
    }

    @ParameterizedTest
    @CsvSource({
            "user,ROLE_USER",
            "admin,ROLE_ADMIN"
    })
    void convertsConfiguredClientRolesToSpringAuthorities(String sourceRole, String expectedAuthority) {
        properties.setClientId("order-taking-api");

        Jwt jwt = Jwt.withTokenValue("token")
                .header("alg", "none")
                .claim("resource_access", Map.of(
                        "order-taking-api", Map.of("roles", List.of(sourceRole)),
                        "other-client", Map.of("roles", List.of("ignored"))
                ))
                .claim("preferred_username", "alice")
                .subject("subject-123")
                .build();

        Authentication authentication = converter.convert(jwt);

        assertThat(authentication.getAuthorities())
                .extracting("authority")
                .contains(expectedAuthority)
                .doesNotContain("ROLE_IGNORED");
        assertThat(authentication.getName()).isEqualTo("alice");
    }

    @ParameterizedTest
    @CsvSource({
            "preferred_username,alice",
            "email,alice@example.com"
    })
    void usesConfiguredPrincipalClaimWhenPresent(String principalClaim, String expectedPrincipal) {
        properties.setPrincipalClaim(principalClaim);

        Jwt jwt = Jwt.withTokenValue("token")
                .header("alg", "none")
                .claim(principalClaim, expectedPrincipal)
                .subject("subject-123")
                .build();

        Authentication authentication = converter.convert(jwt);

        assertThat(authentication.getName()).isEqualTo(expectedPrincipal);
    }
}
