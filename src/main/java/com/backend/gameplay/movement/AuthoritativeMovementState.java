package com.backend.gameplay.movement;

import java.time.Instant;
import java.util.UUID;

public record AuthoritativeMovementState(
        UUID sessionId,
        UUID playerId,
        String zoneId,
        double positionX,
        double positionY,
        MovementDirection direction,
        Instant serverTime,
        Instant lastAcceptedInputTime
) {
}
