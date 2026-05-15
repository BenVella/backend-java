package com.backend.integration;

import com.backend.persistence.player.PlayerLocation;
import com.backend.persistence.player.PlayerLocationRepository;
import com.backend.persistence.player.PlayerProfile;
import com.backend.persistence.player.PlayerProfileRepository;
import com.backend.support.PostgresIntegrationTestSupport;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class PlayerLocationRepositoryIntegrationTest extends PostgresIntegrationTestSupport {

    @Autowired
    private PlayerProfileRepository playerProfileRepository;

    @Autowired
    private PlayerLocationRepository playerLocationRepository;

    @MockitoBean
    private JwtDecoder jwtDecoder;

    @Test
    void upsertsDurablePlayerLocationWithoutCreatingMovementHotPathWrites() {
        PlayerProfile player = playerProfileRepository.create("subject-jinx", "jinx", "Jinx");

        playerLocationRepository.upsert(player.id(), "starter-zone", 1.5, -2.25);
        playerLocationRepository.upsert(player.id(), "starter-zone", 5.0, 6.5);

        Optional<PlayerLocation> stored = playerLocationRepository.findByPlayerId(player.id());

        assertThat(stored).isPresent();
        assertThat(stored.orElseThrow().playerId()).isEqualTo(player.id());
        assertThat(stored.orElseThrow().zoneId()).isEqualTo("starter-zone");
        assertThat(stored.orElseThrow().positionX()).isEqualTo(5.0);
        assertThat(stored.orElseThrow().positionY()).isEqualTo(6.5);
        assertThat(stored.orElseThrow().updatedAt()).isNotNull();
    }
}
