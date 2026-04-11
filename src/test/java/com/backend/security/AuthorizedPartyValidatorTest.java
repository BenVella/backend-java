package com.backend.security;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.security.oauth2.jwt.Jwt;

import static org.assertj.core.api.Assertions.assertThat;

class AuthorizedPartyValidatorTest {

    @Test
    void acceptsTokenContainingAllowedAuthorizedParty() {
        AuthorizedPartyValidator validator = new AuthorizedPartyValidator(List.of("game-backend-cli"));
        Jwt jwt = Jwt.withTokenValue("token")
                .header("alg", "none")
                .claim("azp", "game-backend-cli")
                .build();

        assertThat(validator.validate(jwt).hasErrors()).isFalse();
    }

    @Test
    void rejectsTokenMissingAllowedAuthorizedParty() {
        AuthorizedPartyValidator validator = new AuthorizedPartyValidator(List.of("game-backend-cli"));
        Jwt jwt = Jwt.withTokenValue("token")
                .header("alg", "none")
                .claim("azp", "different-client")
                .build();

        assertThat(validator.validate(jwt).hasErrors()).isTrue();
    }

    @Test
    void skipsValidationWhenNoAuthorizedPartiesAreConfigured() {
        AuthorizedPartyValidator validator = new AuthorizedPartyValidator(List.of());
        Jwt jwt = Jwt.withTokenValue("token")
                .header("alg", "none")
                .subject("subject-123")
                .build();

        assertThat(validator.validate(jwt).hasErrors()).isFalse();
    }
}
