package com.backend.security;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.stereotype.Component;

import static java.util.Locale.ROOT;

@Component
public class JwtAuthConverter implements Converter<Jwt, AbstractAuthenticationToken> {

    private final JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
    private final KeycloakSecurityProperties keycloakSecurityProperties;

    public JwtAuthConverter(KeycloakSecurityProperties keycloakSecurityProperties) {
        this.keycloakSecurityProperties = keycloakSecurityProperties;
    }

    @Override
    public AbstractAuthenticationToken convert(Jwt jwt) {
        Collection<GrantedAuthority> authorities = Stream.concat(
                safeAuthorities(jwtGrantedAuthoritiesConverter.convert(jwt)).stream(),
                extractRoles(jwt).stream()
        ).collect(Collectors.toCollection(LinkedHashSet::new));

        return new JwtAuthenticationToken(jwt, authorities, principalName(jwt));
    }

    private String principalName(Jwt jwt) {
        String principal = jwt.getClaimAsString(keycloakSecurityProperties.getPrincipalClaim());
        if (principal != null && !principal.isBlank()) {
            return principal;
        }

        return jwt.getSubject();
    }

    private Collection<? extends GrantedAuthority> extractRoles(Jwt jwt) {
        return Stream.concat(extractRealmRoles(jwt).stream(), extractClientRoles(jwt).stream())
                .map(role -> role.toUpperCase(ROOT))
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    private Collection<String> extractRealmRoles(Jwt jwt) {
        Map<String, Object> realmAccess = claimAsMap(jwt, "realm_access");
        if (realmAccess == null) {
            return Collections.emptySet();
        }

        return claimRoles(realmAccess.get("roles"));
    }

    private Collection<String> extractClientRoles(Jwt jwt) {
        Map<String, Object> resourceAccess = claimAsMap(jwt, "resource_access");
        if (resourceAccess == null) {
            return Collections.emptySet();
        }

        Object clientAccess = resourceAccess.get(keycloakSecurityProperties.getClientId());
        if (!(clientAccess instanceof Map<?, ?> clientMap)) {
            return Collections.emptySet();
        }

        return claimRoles(clientMap.get("roles"));
    }

    private Map<String, Object> claimAsMap(Jwt jwt, String claimName) {
        Object claim = jwt.getClaim(claimName);
        if (claim instanceof Map<?, ?> rawMap) {
            return rawMap.entrySet().stream()
                    .filter(entry -> entry.getKey() instanceof String)
                    .collect(Collectors.toMap(
                            entry -> (String) entry.getKey(),
                            Map.Entry::getValue
                    ));
        }

        return null;
    }

    private Collection<String> claimRoles(Object claim) {
        if (!(claim instanceof Collection<?> rawRoles)) {
            return Collections.emptySet();
        }

        return rawRoles.stream()
                .filter(String.class::isInstance)
                .map(String.class::cast)
                .filter(Objects::nonNull)
                .filter(role -> !role.isBlank())
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    private Collection<GrantedAuthority> safeAuthorities(Collection<GrantedAuthority> authorities) {
        if (authorities == null) {
            return List.of();
        }

        return authorities;
    }
}
