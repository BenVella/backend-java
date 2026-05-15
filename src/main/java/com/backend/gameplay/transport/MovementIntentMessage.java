package com.backend.gameplay.transport;

import com.backend.gameplay.movement.MovementDirection;

import java.time.Instant;

public record MovementIntentMessage(
        Instant inputTime,
        MovementDirection direction
) {
}
