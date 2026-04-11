package com.backend.integration;

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
class PlayerProfileRepositoryIntegrationTest extends PostgresIntegrationTestSupport {

    @Autowired
    private PlayerProfileRepository repository;

    @MockitoBean
    private JwtDecoder jwtDecoder;

    @Test
    void createsAndReadsPlayerProfilesUsingPostgres() {
        PlayerProfile created = repository.create("subject-riven", "riven-main", "Riven Main");

        Optional<PlayerProfile> byId = repository.findById(created.id());
        Optional<PlayerProfile> byHandle = repository.findByHandle(created.handle());
        Optional<PlayerProfile> bySubject = repository.findByExternalSubject(created.externalSubject());

        assertThat(byId).isPresent();
        assertThat(byHandle).isPresent();
        assertThat(bySubject).isPresent();
        assertThat(byId.orElseThrow().id()).isEqualTo(created.id());
        assertThat(byId.orElseThrow().externalSubject()).isEqualTo(created.externalSubject());
        assertThat(byId.orElseThrow().handle()).isEqualTo(created.handle());
        assertThat(byId.orElseThrow().displayName()).isEqualTo(created.displayName());
        assertThat(byHandle.orElseThrow().id()).isEqualTo(created.id());
        assertThat(byHandle.orElseThrow().externalSubject()).isEqualTo(created.externalSubject());
        assertThat(byHandle.orElseThrow().handle()).isEqualTo(created.handle());
        assertThat(byHandle.orElseThrow().displayName()).isEqualTo(created.displayName());
        assertThat(bySubject.orElseThrow().id()).isEqualTo(created.id());
        assertThat(bySubject.orElseThrow().externalSubject()).isEqualTo(created.externalSubject());
        assertThat(repository.count()).isEqualTo(1);
        assertThat(repository.findAll())
                .singleElement()
                .extracting(PlayerProfile::id, PlayerProfile::externalSubject, PlayerProfile::handle, PlayerProfile::displayName)
                .containsExactly(created.id(), created.externalSubject(), created.handle(), created.displayName());
    }
}
