package com.backend.gameplay.transport;

import com.backend.gameplay.movement.AuthoritativeMovementState;

public record GameplaySocketMessage(
        String type,
        AuthoritativeMovementState state
) {
}
