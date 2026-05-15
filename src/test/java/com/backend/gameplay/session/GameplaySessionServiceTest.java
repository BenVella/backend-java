package com.backend.gameplay.session;

import org.junit.jupiter.api.Test;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class GameplaySessionServiceTest {

    private final GameplaySessionProperties properties = new GameplaySessionProperties();
    private final Clock clock = Clock.fixed(Instant.parse("2026-04-11T08:00:00Z"), ZoneOffset.UTC);
    private final GameplaySessionService service = new GameplaySessionService(properties, clock);

    @Test
    void ignoresCloseRequestsForDifferentConnectionId() {
        UUID playerId = UUID.randomUUID();
        GameplaySessionTicket ticket = service.issue(playerId, "ahri");

        assertThat(service.attach(ticket.sessionId(), "active-connection")).isTrue();

        Optional<GameplaySessionTicket> closed = service.closeAttached(ticket.sessionId(), "replayed-connection");

        assertThat(closed).isEmpty();
        assertThat(service.validate(ticket.sessionId(), ticket.sessionToken())).contains(ticket);
    }

    @Test
    void closesSessionForAttachedConnectionId() {
        UUID playerId = UUID.randomUUID();
        GameplaySessionTicket ticket = service.issue(playerId, "ahri");

        assertThat(service.attach(ticket.sessionId(), "active-connection")).isTrue();

        Optional<GameplaySessionTicket> closed = service.closeAttached(ticket.sessionId(), "active-connection");

        assertThat(closed).contains(ticket);
        assertThat(service.validate(ticket.sessionId(), ticket.sessionToken())).isEmpty();
        assertThat(service.findActiveSessionId(playerId)).isEmpty();
    }
}
