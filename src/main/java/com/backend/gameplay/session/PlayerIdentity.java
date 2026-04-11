package com.backend.gameplay.session;

import java.util.UUID;

public record PlayerIdentity(
        UUID playerId,
        String externalSubject,
        String handle,
        String displayName
) {
}
