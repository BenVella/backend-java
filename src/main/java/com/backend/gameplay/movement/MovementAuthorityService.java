package com.backend.gameplay.movement;

import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.UUID;

@Service
public class MovementAuthorityService {

    private final GameplayMovementProperties properties;

    public MovementAuthorityService(GameplayMovementProperties properties) {
        this.properties = properties;
    }

    public ActiveMovementState initialize(UUID sessionId, UUID playerId, String zoneId, double positionX, double positionY, Instant now) {
        return new ActiveMovementState(
                sessionId,
                playerId,
                zoneId,
                positionX,
                positionY,
                MovementDirection.NONE,
                now,
                null
        );
    }

    public AuthoritativeMovementState snapshot(ActiveMovementState state, Instant now) {
        advanceTo(state, now);
        return toSnapshot(state);
    }

    public AuthoritativeMovementState applyIntent(ActiveMovementState state, MovementIntent intent, Instant now) {
        advanceTo(state, now);

        Instant inputTime = intent.inputTime();
        if (inputTime != null && isAccepted(state.lastAcceptedInputTime(), inputTime)) {
            state.setDirection(intent.direction());
            state.setLastAcceptedInputTime(inputTime);
        }

        return toSnapshot(state);
    }

    private boolean isAccepted(Instant lastAcceptedInputTime, Instant inputTime) {
        return lastAcceptedInputTime == null || !inputTime.isBefore(lastAcceptedInputTime);
    }

    private void advanceTo(ActiveMovementState state, Instant now) {
        Duration elapsed = Duration.between(state.lastSimulatedAt(), now);
        if (elapsed.isNegative() || elapsed.isZero()) {
            state.setLastSimulatedAt(now);
            return;
        }

        double seconds = elapsed.toNanos() / 1_000_000_000d;
        double distance = properties.getSpeedUnitsPerSecond() * seconds;

        state.setPositionX(state.positionX() + (state.direction().xComponent() * distance));
        state.setPositionY(state.positionY() + (state.direction().yComponent() * distance));
        state.setLastSimulatedAt(now);
    }

    private AuthoritativeMovementState toSnapshot(ActiveMovementState state) {
        return new AuthoritativeMovementState(
                state.sessionId(),
                state.playerId(),
                state.zoneId(),
                state.positionX(),
                state.positionY(),
                state.direction(),
                state.lastSimulatedAt(),
                state.lastAcceptedInputTime()
        );
    }
}
