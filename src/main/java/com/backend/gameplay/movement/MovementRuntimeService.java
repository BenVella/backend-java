package com.backend.gameplay.movement;

import org.springframework.stereotype.Service;

import java.time.Clock;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
public class MovementRuntimeService {

    private final MovementAuthorityService movementAuthorityService;
    private final Clock clock;
    private final Map<UUID, ActiveMovementState> activeStates = new HashMap<>();

    public MovementRuntimeService(MovementAuthorityService movementAuthorityService, Clock clock) {
        this.movementAuthorityService = movementAuthorityService;
        this.clock = clock;
    }

    public synchronized AuthoritativeMovementState startSession(
            UUID sessionId,
            UUID playerId,
            String zoneId,
            double positionX,
            double positionY
    ) {
        ActiveMovementState state = movementAuthorityService.initialize(
                sessionId,
                playerId,
                zoneId,
                positionX,
                positionY,
                Instant.now(clock)
        );
        activeStates.put(sessionId, state);
        return movementAuthorityService.snapshot(state, Instant.now(clock));
    }

    public synchronized Optional<AuthoritativeMovementState> snapshot(UUID sessionId) {
        ActiveMovementState state = activeStates.get(sessionId);
        if (state == null) {
            return Optional.empty();
        }

        return Optional.of(movementAuthorityService.snapshot(state, Instant.now(clock)));
    }

    public synchronized Optional<AuthoritativeMovementState> applyIntent(MovementIntent intent) {
        ActiveMovementState state = activeStates.get(intent.sessionId());
        if (state == null) {
            return Optional.empty();
        }

        return Optional.of(movementAuthorityService.applyIntent(state, intent, Instant.now(clock)));
    }

    public synchronized Optional<AuthoritativeMovementState> stopSession(UUID sessionId) {
        ActiveMovementState state = activeStates.remove(sessionId);
        if (state == null) {
            return Optional.empty();
        }

        return Optional.of(movementAuthorityService.snapshot(state, Instant.now(clock)));
    }
}
