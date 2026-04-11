package com.backend.security;

import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app.security.keycloak")
public class KeycloakSecurityProperties {

    private String clientId = "game-backend";
    private String requiredAudience = "game-backend";
    private List<String> allowedAuthorizedParties = List.of("game-backend-cli");
    private String principalClaim = "preferred_username";

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getRequiredAudience() {
        return requiredAudience;
    }

    public void setRequiredAudience(String requiredAudience) {
        this.requiredAudience = requiredAudience;
    }

    public List<String> getAllowedAuthorizedParties() {
        return allowedAuthorizedParties;
    }

    public void setAllowedAuthorizedParties(List<String> allowedAuthorizedParties) {
        this.allowedAuthorizedParties = allowedAuthorizedParties;
    }

    public String getPrincipalClaim() {
        return principalClaim;
    }

    public void setPrincipalClaim(String principalClaim) {
        this.principalClaim = principalClaim;
    }
}
