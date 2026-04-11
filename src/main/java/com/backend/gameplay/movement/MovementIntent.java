package com.backend.gameplay.movement;

import java.time.Instant;
import java.util.UUID;

public record MovementIntent(
        UUID sessionId,
        Instant inputTime,
        MovementDirection direction
) {
}
