package com.backend.security;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;

import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2TokenValidatorResult;
import org.springframework.security.oauth2.jwt.Jwt;

public class AuthorizedPartyValidator implements OAuth2TokenValidator<Jwt> {

    private static final OAuth2Error ERROR = new OAuth2Error(
            "invalid_token",
            "The token does not contain an allowed authorized party",
            null
    );

    private final Set<String> allowedAuthorizedParties;

    public AuthorizedPartyValidator(Collection<String> allowedAuthorizedParties) {
        this.allowedAuthorizedParties = allowedAuthorizedParties.stream()
                .map(String::trim)
                .filter(value -> !value.isBlank())
                .collect(LinkedHashSet::new, Set::add, Set::addAll);
    }

    @Override
    public OAuth2TokenValidatorResult validate(Jwt token) {
        if (allowedAuthorizedParties.isEmpty()) {
            return OAuth2TokenValidatorResult.success();
        }

        String authorizedParty = token.getClaimAsString("azp");
        if (authorizedParty != null && allowedAuthorizedParties.contains(authorizedParty)) {
            return OAuth2TokenValidatorResult.success();
        }

        return OAuth2TokenValidatorResult.failure(ERROR);
    }
}
