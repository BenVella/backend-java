package com.backend.security;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class JwtAuthConverterTest {

    private final JwtAuthConverter converter = new JwtAuthConverter();

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
}
