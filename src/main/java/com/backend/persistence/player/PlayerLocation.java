package com.backend.persistence.player;

import java.time.OffsetDateTime;
import java.util.UUID;

public record PlayerLocation(
        UUID playerId,
        String zoneId,
        double positionX,
        double positionY,
        OffsetDateTime updatedAt
) {
}
