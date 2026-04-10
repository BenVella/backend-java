package com.backend.integration;

import com.backend.persistence.player.PlayerProfile;
import com.backend.persistence.player.PlayerProfileRepository;
import com.backend.support.PostgresIntegrationTestSupport;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class PlayerProfileRepositoryIntegrationTest extends PostgresIntegrationTestSupport {

    @Autowired
    private PlayerProfileRepository repository;

    @Test
    void createsAndReadsPlayerProfilesUsingPostgres() {
        PlayerProfile created = repository.create("riven-main", "Riven Main");

        Optional<PlayerProfile> byId = repository.findById(created.id());
        Optional<PlayerProfile> byHandle = repository.findByHandle(created.handle());

        assertThat(byId).contains(created);
        assertThat(byHandle).contains(created);
        assertThat(repository.count()).isEqualTo(1);
        assertThat(repository.findAll()).containsExactly(created);
    }
}
