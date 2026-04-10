package com.backend.integration;

import com.backend.support.PostgresIntegrationTestSupport;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class OpenApiIntegrationTest extends PostgresIntegrationTestSupport {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private JwtDecoder jwtDecoder;

    @Test
    void publishesAccessProbeContractAndBearerSecurityScheme() throws Exception {
        mockMvc.perform(get("/v3/api-docs"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.info.title").value("Game Backend API"))
                .andExpect(jsonPath("$.paths['/api/ping']").exists())
                .andExpect(jsonPath("$.paths['/api/access/public']").exists())
                .andExpect(jsonPath("$.paths['/api/access/public'].get.security").doesNotExist())
                .andExpect(jsonPath("$.paths['/api/access/user']").exists())
                .andExpect(jsonPath("$.paths['/api/access/user'].get.security[0].bearerAuth").exists())
                .andExpect(jsonPath("$.paths['/api/access/admin']").exists())
                .andExpect(jsonPath("$.components.securitySchemes.bearerAuth.scheme").value("bearer"))
                .andExpect(jsonPath("$.components.schemas.StatusResponse").exists())
                .andExpect(jsonPath("$.components.schemas.AccessResponse").exists())
                .andExpect(jsonPath("$.components.schemas.ApiErrorResponse").exists());
    }
}
