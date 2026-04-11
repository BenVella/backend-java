package com.backend.gameplay.movement;

import java.time.Instant;
import java.util.UUID;

final class ActiveMovementState {

    private final UUID sessionId;
    private final UUID playerId;
    private final String zoneId;
    private double positionX;
    private double positionY;
    private MovementDirection direction;
    private Instant lastSimulatedAt;
    private Instant lastAcceptedInputTime;

    ActiveMovementState(
            UUID sessionId,
            UUID playerId,
            String zoneId,
            double positionX,
            double positionY,
            MovementDirection direction,
            Instant lastSimulatedAt,
            Instant lastAcceptedInputTime
    ) {
        this.sessionId = sessionId;
        this.playerId = playerId;
        this.zoneId = zoneId;
        this.positionX = positionX;
        this.positionY = positionY;
        this.direction = direction;
        this.lastSimulatedAt = lastSimulatedAt;
        this.lastAcceptedInputTime = lastAcceptedInputTime;
    }

    UUID sessionId() {
        return sessionId;
    }

    UUID playerId() {
        return playerId;
    }

    String zoneId() {
        return zoneId;
    }

    double positionX() {
        return positionX;
    }

    void setPositionX(double positionX) {
        this.positionX = positionX;
    }

    double positionY() {
        return positionY;
    }

    void setPositionY(double positionY) {
        this.positionY = positionY;
    }

    MovementDirection direction() {
        return direction;
    }

    void setDirection(MovementDirection direction) {
        this.direction = direction;
    }

    Instant lastSimulatedAt() {
        return lastSimulatedAt;
    }

    void setLastSimulatedAt(Instant lastSimulatedAt) {
        this.lastSimulatedAt = lastSimulatedAt;
    }

    Instant lastAcceptedInputTime() {
        return lastAcceptedInputTime;
    }

    void setLastAcceptedInputTime(Instant lastAcceptedInputTime) {
        this.lastAcceptedInputTime = lastAcceptedInputTime;
    }
}
