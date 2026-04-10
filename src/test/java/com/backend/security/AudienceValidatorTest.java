package com.backend.security;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.security.oauth2.jwt.Jwt;

import static org.assertj.core.api.Assertions.assertThat;

class AudienceValidatorTest {

    @Test
    void acceptsTokenContainingRequiredAudience() {
        AudienceValidator validator = new AudienceValidator("order-taking-api");
        Jwt jwt = Jwt.withTokenValue("token")
                .header("alg", "none")
                .audience(List.of("account", "order-taking-api"))
                .build();

        assertThat(validator.validate(jwt).hasErrors()).isFalse();
    }

    @Test
    void rejectsTokenMissingRequiredAudience() {
        AudienceValidator validator = new AudienceValidator("order-taking-api");
        Jwt jwt = Jwt.withTokenValue("token")
                .header("alg", "none")
                .audience(List.of("account"))
                .build();

        assertThat(validator.validate(jwt).hasErrors()).isTrue();
    }
}
