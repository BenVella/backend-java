package com.backend.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtDecoders;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collections;
import java.util.Map;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private static final Logger log = LoggerFactory.getLogger(SecurityConfig.class);
    @Value("${spring.security.oauth2.resourceserver.jwt.issuer-uri}")
    private String issuerUri;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        // Disable CSRF protection
        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(request -> request.anyRequest().authenticated())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .oauth2ResourceServer(oauth2 -> oauth2.jwt(jwt -> {
                    jwt.decoder(jwtDecoder());
                })); // Enable custom JWT validation for readouts

        return http.build();
    }

//
//    @Bean
//    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//        http
//                .csrf(AbstractHttpConfigurer::disable)
//                .authorizeHttpRequests(authorize -> {
//                    authorize.requestMatchers("/").permitAll();
//                    authorize.requestMatchers("/webjars/**").permitAll();
//                    authorize.requestMatchers("/error").permitAll();
//                    authorize.anyRequest().authenticated();
//                })

    /// /        .oauth2ResourceServer(oauth2 -> oauth2.jwt(Customizer.withDefaults())); // Enable JWT validation
//                .oauth2Login(Customizer.withDefaults())
//                .oauth2ResourceServer(oauth2 -> oauth2.jwt(jwt -> {
//                    jwt.decoder(jwtDecoder());
//                })); // Enable JWT validation
//        return http.build();
//    }
    @Bean
    public JwtDecoder jwtDecoder() {
        return token -> {
            try {
                Jwt jwt = JwtDecoders.fromIssuerLocation(issuerUri).decode(token);
                // Log token details for debugging
                log.info("JwtDecoder: Token Valid with claims: {}", jwt.getClaims());
                return jwt;
            } catch (JwtException e) {
                // Log the exception for debugging
                log.error("JwtDecoder: Token Invalid: {}", e.getMessage());
                throw e;
            }
        };
    }

    @GetMapping("/user")
    @ResponseBody
    public Map<String, Object> user(@AuthenticationPrincipal OAuth2User principal) {
        if (principal == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User not authenticated");
        }
        return Collections.singletonMap("name", principal.getAttribute("name"));
    }

    @GetMapping("/secure-details")
    public String secureDetailsEndpoint(@AuthenticationPrincipal Jwt jwt) {
        String email = jwt.getClaim("email");
        return "Hello, your email is " + email;
    }

}
