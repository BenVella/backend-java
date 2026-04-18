package com.backend.gameplay.session;

import com.backend.gameplay.movement.AuthoritativeMovementState;
import com.backend.gameplay.movement.GameplayMovementProperties;
import com.backend.gameplay.movement.MovementDirection;
import com.backend.gameplay.movement.MovementIntent;
import com.backend.gameplay.movement.MovementRuntimeService;
import com.backend.persistence.player.PlayerLocation;
import com.backend.persistence.player.PlayerLocationRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
public class GameplaySessionCoordinator {

    private final PlayerIdentityService playerIdentityService;
    private final GameplaySessionService gameplaySessionService;
    private final PlayerLocationRepository playerLocationRepository;
    private final MovementRuntimeService movementRuntimeService;
    private final GameplaySessionProperties sessionProperties;
    private final GameplayMovementProperties movementProperties;

    public GameplaySessionCoordinator(
            PlayerIdentityService playerIdentityService,
            GameplaySessionService gameplaySessionService,
            PlayerLocationRepository playerLocationRepository,
            MovementRuntimeService movementRuntimeService,
            GameplaySessionProperties sessionProperties,
            GameplayMovementProperties movementProperties
    ) {
        this.playerIdentityService = playerIdentityService;
        this.gameplaySessionService = gameplaySessionService;
        this.playerLocationRepository = playerLocationRepository;
        this.movementRuntimeService = movementRuntimeService;
        this.sessionProperties = sessionProperties;
        this.movementProperties = movementProperties;
    }

    public GameplaySessionResponse issueSession(String externalSubject, String preferredHandle) {
        PlayerIdentity playerIdentity = playerIdentityService.resolvePlayer(externalSubject, preferredHandle);
        closeExistingSession(playerIdentity.playerId());

        PlayerLocation startingLocation = playerLocationRepository.findByPlayerId(playerIdentity.playerId())
                .orElseGet(() -> new PlayerLocation(
                        playerIdentity.playerId(),
                        movementProperties.getDefaultZoneId(),
                        0.0,
                        0.0,
                        null
                ));

        GameplaySessionTicket sessionTicket = gameplaySessionService.issue(playerIdentity.playerId(), playerIdentity.handle());
        AuthoritativeMovementState initialState = movementRuntimeService.startSession(
                sessionTicket.sessionId(),
                playerIdentity.playerId(),
                startingLocation.zoneId(),
                startingLocation.positionX(),
                startingLocation.positionY()
        );

        return new GameplaySessionResponse(
                sessionTicket.sessionId(),
                sessionTicket.playerId(),
                sessionTicket.playerHandle(),
                sessionTicket.sessionToken(),
                sessionTicket.expiresAt(),
                "websocket",
                sessionProperties.getWebsocketPath(),
                initialState
        );
    }

    public Optional<GameplaySessionTicket> validateConnection(UUID sessionId, String sessionToken) {
        return gameplaySessionService.validate(sessionId, sessionToken);
    }

    public boolean attachConnection(UUID sessionId, String connectionId) {
        return gameplaySessionService.attach(sessionId, connectionId);
    }

    public Optional<AuthoritativeMovementState> currentState(UUID sessionId) {
        return movementRuntimeService.snapshot(sessionId);
    }

    public Optional<AuthoritativeMovementState> applyIntent(UUID sessionId, Instant inputTime, MovementDirection direction) {
        return movementRuntimeService.applyIntent(new MovementIntent(sessionId, inputTime, direction));
    }

    public void closeSession(UUID sessionId) {
        Optional<AuthoritativeMovementState> finalState = movementRuntimeService.stopSession(sessionId);
        gameplaySessionService.close(sessionId);
        finalState.ifPresent(this::persistLocation);
    }

    public boolean closeConnection(UUID sessionId, String connectionId) {
        Optional<GameplaySessionTicket> closedSession = gameplaySessionService.closeAttached(sessionId, connectionId);
        if (closedSession.isEmpty()) {
            return false;
        }

        movementRuntimeService.stopSession(sessionId).ifPresent(this::persistLocation);
        return true;
    }

    private void closeExistingSession(UUID playerId) {
        gameplaySessionService.findActiveSessionId(playerId).ifPresent(this::closeSession);
    }

    private void persistLocation(AuthoritativeMovementState state) {
        playerLocationRepository.upsert(
                state.playerId(),
                state.zoneId(),
                state.positionX(),
                state.positionY()
        );
    }
}
