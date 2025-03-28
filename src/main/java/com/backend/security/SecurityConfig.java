package com.backend.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * See <a href="https://github.com/av24soft/keycloak/blob/main/Keyclockdemo">av24soft Keycloak Demo</a> for more info
 */
@Configuration
public class SecurityConfig {

    final JwtAuthConverter authConverter;

    public SecurityConfig(JwtAuthConverter authConverter) {
        this.authConverter = authConverter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/helloAdmin").hasRole("ADMIN")
                        .requestMatchers("/helloUser").hasRole("USER")
                        .anyRequest().authenticated()
                )
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(jwt -> jwt
                                .jwtAuthenticationConverter(authConverter)
                        )
                );

        return http.build();
    }

    @GetMapping("/helloUser")
    public String helloUser()
    {

        return "helloUser";
    }

    @GetMapping("/helloAdmin")
    public String helloAdmin()
    {

        return "helloAdmin";
    }
}
