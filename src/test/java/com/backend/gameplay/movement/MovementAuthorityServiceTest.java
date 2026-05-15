package com.backend.gameplay.movement;

import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.offset;

class MovementAuthorityServiceTest {

    private final GameplayMovementProperties properties = new GameplayMovementProperties();
    private final MovementAuthorityService movementAuthorityService = new MovementAuthorityService(properties);

    @Test
    void computesAuthoritativePositionFromAcceptedIntentAndElapsedTime() {
        Instant start = Instant.parse("2026-04-11T08:00:00Z");
        ActiveMovementState state = movementAuthorityService.initialize(
                UUID.randomUUID(),
                UUID.randomUUID(),
                "starter-zone",
                0.0,
                0.0,
                start
        );

        movementAuthorityService.applyIntent(
                state,
                new MovementIntent(state.sessionId(), start, MovementDirection.EAST),
                start
        );

        AuthoritativeMovementState moved = movementAuthorityService.snapshot(state, start.plusSeconds(1));
        AuthoritativeMovementState stopped = movementAuthorityService.applyIntent(
                state,
                new MovementIntent(state.sessionId(), start.plusMillis(1500), MovementDirection.NONE),
                start.plusMillis(1500)
        );
        AuthoritativeMovementState afterStop = movementAuthorityService.snapshot(state, start.plusSeconds(2));

        assertThat(moved.positionX()).isCloseTo(4.0, offset(0.0001));
        assertThat(moved.positionY()).isZero();
        assertThat(stopped.positionX()).isCloseTo(6.0, offset(0.0001));
        assertThat(stopped.direction()).isEqualTo(MovementDirection.NONE);
        assertThat(afterStop.positionX()).isCloseTo(6.0, offset(0.0001));
        assertThat(afterStop.positionY()).isZero();
    }

    @Test
    void normalizesDiagonalMovementSpeed() {
        Instant start = Instant.parse("2026-04-11T08:00:00Z");
        ActiveMovementState state = movementAuthorityService.initialize(
                UUID.randomUUID(),
                UUID.randomUUID(),
                "starter-zone",
                0.0,
                0.0,
                start
        );

        movementAuthorityService.applyIntent(
                state,
                new MovementIntent(state.sessionId(), start, MovementDirection.NORTH_EAST),
                start
        );

        AuthoritativeMovementState moved = movementAuthorityService.snapshot(state, start.plusSeconds(1));

        assertThat(moved.positionX()).isCloseTo(4.0 / Math.sqrt(2.0), offset(0.0001));
        assertThat(moved.positionY()).isCloseTo(4.0 / Math.sqrt(2.0), offset(0.0001));
    }

    @Test
    void ignoresOutOfOrderMovementIntentTimestamps() {
        Instant start = Instant.parse("2026-04-11T08:00:00Z");
        ActiveMovementState state = movementAuthorityService.initialize(
                UUID.randomUUID(),
                UUID.randomUUID(),
                "starter-zone",
                0.0,
                0.0,
                start
        );

        movementAuthorityService.applyIntent(
                state,
                new MovementIntent(state.sessionId(), start.plusMillis(100), MovementDirection.EAST),
                start.plusMillis(100)
        );

        AuthoritativeMovementState afterLateIntent = movementAuthorityService.applyIntent(
                state,
                new MovementIntent(state.sessionId(), start.plusMillis(50), MovementDirection.NORTH),
                start.plusMillis(1100)
        );
        AuthoritativeMovementState finalState = movementAuthorityService.snapshot(state, start.plusMillis(2100));

        assertThat(afterLateIntent.direction()).isEqualTo(MovementDirection.EAST);
        assertThat(afterLateIntent.positionX()).isCloseTo(4.0, offset(0.0001));
        assertThat(finalState.direction()).isEqualTo(MovementDirection.EAST);
        assertThat(finalState.positionX()).isCloseTo(8.0, offset(0.0001));
        assertThat(finalState.positionY()).isZero();
    }
}
