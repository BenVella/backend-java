package com.backend.gameplay.session;

import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.Clock;
import java.time.Instant;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
public class GameplaySessionService {

    private final GameplaySessionProperties properties;
    private final Clock clock;
    private final SecureRandom secureRandom = new SecureRandom();
    private final Map<UUID, StoredGameplaySession> sessionsById = new HashMap<>();
    private final Map<UUID, UUID> sessionIdByPlayerId = new HashMap<>();

    public GameplaySessionService(GameplaySessionProperties properties, Clock clock) {
        this.properties = properties;
        this.clock = clock;
    }

    public synchronized GameplaySessionTicket issue(UUID playerId, String playerHandle) {
        Instant issuedAt = Instant.now(clock);
        Instant expiresAt = issuedAt.plus(properties.getTokenTtl());
        UUID sessionId = UUID.randomUUID();
        String sessionToken = generateSessionToken();

        StoredGameplaySession stored = new StoredGameplaySession(
                sessionId,
                playerId,
                playerHandle,
                sessionToken,
                issuedAt,
                expiresAt
        );
        sessionsById.put(sessionId, stored);
        sessionIdByPlayerId.put(playerId, sessionId);
        return stored.toTicket();
    }

    public synchronized Optional<UUID> findActiveSessionId(UUID playerId) {
        return Optional.ofNullable(sessionIdByPlayerId.get(playerId));
    }

    public synchronized Optional<GameplaySessionTicket> validate(UUID sessionId, String sessionToken) {
        StoredGameplaySession stored = sessionsById.get(sessionId);
        if (stored == null || !stored.matches(sessionToken) || stored.isExpired(Instant.now(clock))) {
            return Optional.empty();
        }

        return Optional.of(stored.toTicket());
    }

    public synchronized boolean attach(UUID sessionId, String connectionId) {
        StoredGameplaySession stored = sessionsById.get(sessionId);
        if (stored == null || stored.connectionId != null) {
            return false;
        }

        stored.connectionId = connectionId;
        return true;
    }

    public synchronized Optional<GameplaySessionTicket> close(UUID sessionId) {
        StoredGameplaySession stored = sessionsById.remove(sessionId);
        if (stored == null) {
            return Optional.empty();
        }

        sessionIdByPlayerId.remove(stored.playerId, sessionId);
        return Optional.of(stored.toTicket());
    }

    public synchronized Optional<GameplaySessionTicket> closeAttached(UUID sessionId, String connectionId) {
        StoredGameplaySession stored = sessionsById.get(sessionId);
        if (stored == null || stored.connectionId == null || !stored.connectionId.equals(connectionId)) {
            return Optional.empty();
        }

        sessionsById.remove(sessionId);
        sessionIdByPlayerId.remove(stored.playerId, sessionId);
        return Optional.of(stored.toTicket());
    }

    private String generateSessionToken() {
        byte[] bytes = new byte[24];
        secureRandom.nextBytes(bytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }

    private static final class StoredGameplaySession {
        private final UUID sessionId;
        private final UUID playerId;
        private final String playerHandle;
        private final String sessionToken;
        private final Instant issuedAt;
        private final Instant expiresAt;
        private String connectionId;

        private StoredGameplaySession(
                UUID sessionId,
                UUID playerId,
                String playerHandle,
                String sessionToken,
                Instant issuedAt,
                Instant expiresAt
        ) {
            this.sessionId = sessionId;
            this.playerId = playerId;
            this.playerHandle = playerHandle;
            this.sessionToken = sessionToken;
            this.issuedAt = issuedAt;
            this.expiresAt = expiresAt;
        }

        private boolean matches(String candidateToken) {
            return sessionToken.equals(candidateToken);
        }

        private boolean isExpired(Instant now) {
            return expiresAt.isBefore(now);
        }

        private GameplaySessionTicket toTicket() {
            return new GameplaySessionTicket(sessionId, playerId, playerHandle, sessionToken, issuedAt, expiresAt);
        }
    }
}
