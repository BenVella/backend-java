package com.backend.integration;

import com.backend.persistence.player.PlayerProfileRepository;
import com.backend.support.PostgresIntegrationTestSupport;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class GameplaySessionIntegrationTest extends PostgresIntegrationTestSupport {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PlayerProfileRepository playerProfileRepository;

    @MockitoBean
    private JwtDecoder jwtDecoder;

    @Test
    void issuesGameplaySessionForAuthenticatedPlayer() throws Exception {
        mockMvc.perform(post("/api/gameplay/sessions")
                        .with(jwt().jwt(jwt -> jwt
                                .subject("subject-ahri")
                                .claim("preferred_username", "ahri"))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.playerHandle").value("ahri"))
                .andExpect(jsonPath("$.transport").value("websocket"))
                .andExpect(jsonPath("$.websocketPath").value("/ws/gameplay"))
                .andExpect(jsonPath("$.sessionId").isNotEmpty())
                .andExpect(jsonPath("$.sessionToken").isNotEmpty())
                .andExpect(jsonPath("$.initialState.zoneId").value("starter-zone"))
                .andExpect(jsonPath("$.initialState.positionX").value(0.0))
                .andExpect(jsonPath("$.initialState.positionY").value(0.0));

        assertThat(playerProfileRepository.findByExternalSubject("subject-ahri")).isPresent();
        assertThat(playerProfileRepository.findByExternalSubject("subject-ahri").orElseThrow().handle()).isEqualTo("ahri");
    }
}
