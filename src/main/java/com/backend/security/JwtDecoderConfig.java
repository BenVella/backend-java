package com.backend.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.core.DelegatingOAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtDecoders;
import org.springframework.security.oauth2.jwt.JwtValidators;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;

@Configuration
@EnableConfigurationProperties(KeycloakSecurityProperties.class)
public class JwtDecoderConfig {

    @Bean
    public JwtDecoder jwtDecoder(
            @Value("${spring.security.oauth2.resourceserver.jwt.issuer-uri}") String issuerUri,
            KeycloakSecurityProperties keycloakSecurityProperties
    ) {
        NimbusJwtDecoder jwtDecoder = (NimbusJwtDecoder) JwtDecoders.fromIssuerLocation(issuerUri);

        OAuth2TokenValidator<Jwt> withIssuer = JwtValidators.createDefaultWithIssuer(issuerUri);
        OAuth2TokenValidator<Jwt> withAudience = new AudienceValidator(
                keycloakSecurityProperties.getRequiredAudience()
        );
        OAuth2TokenValidator<Jwt> withAuthorizedParty = new AuthorizedPartyValidator(
                keycloakSecurityProperties.getAllowedAuthorizedParties()
        );

        jwtDecoder.setJwtValidator(new DelegatingOAuth2TokenValidator<>(
                withIssuer,
                withAudience,
                withAuthorizedParty
        ));
        return jwtDecoder;
    }
}
