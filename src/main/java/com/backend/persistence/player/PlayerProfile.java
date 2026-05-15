package com.backend.persistence.player;

import java.time.OffsetDateTime;
import java.util.UUID;

public record PlayerProfile(
        UUID id,
        String externalSubject,
        String handle,
        String displayName,
        OffsetDateTime createdAt,
        OffsetDateTime updatedAt
) {
}
