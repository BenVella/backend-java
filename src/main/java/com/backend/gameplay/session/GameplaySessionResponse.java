package com.backend.gameplay.session;

import com.backend.gameplay.movement.AuthoritativeMovementState;

import java.time.Instant;
import java.util.UUID;

public record GameplaySessionResponse(
        UUID sessionId,
        UUID playerId,
        String playerHandle,
        String sessionToken,
        Instant expiresAt,
        String transport,
        String websocketPath,
        AuthoritativeMovementState initialState
) {
}
