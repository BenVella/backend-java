package com.backend.gameplay.session;

import java.time.Instant;
import java.util.UUID;

public record GameplaySessionTicket(
        UUID sessionId,
        UUID playerId,
        String playerHandle,
        String sessionToken,
        Instant issuedAt,
        Instant expiresAt
) {
}
