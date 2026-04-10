package com.backend.integration;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class SecurityIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private JwtDecoder jwtDecoder;

    @Test
    void allowsPublicEndpointsWithoutAuthentication() throws Exception {
        mockMvc.perform(get("/api/ping")).andExpect(status().isOk());
        mockMvc.perform(get("/helloGuest")).andExpect(status().isOk());
        mockMvc.perform(get("/actuator/health/readiness")).andExpect(status().isOk());
        mockMvc.perform(get("/v3/api-docs")).andExpect(status().isOk());
        mockMvc.perform(get("/swagger-ui/index.html")).andExpect(status().isOk());
    }

    @Test
    void rejectsProtectedEndpointWithoutAuthentication() throws Exception {
        mockMvc.perform(get("/helloUser")).andExpect(status().isUnauthorized());
    }

    @Test
    void allowsHelloUserForUserRoleToken() throws Exception {
        mockMvc.perform(get("/helloUser").with(jwt().authorities(() -> "ROLE_USER")))
                .andExpect(status().isOk());
    }

    @Test
    void rejectsHelloAdminForNonAdminRoleToken() throws Exception {
        mockMvc.perform(get("/helloAdmin").with(jwt().authorities(() -> "ROLE_USER")))
                .andExpect(status().isForbidden());
    }
}
