package com.backend.gameplay.transport;

import com.backend.gameplay.movement.AuthoritativeMovementState;
import com.backend.gameplay.movement.MovementDirection;
import com.backend.gameplay.session.GameplaySessionCoordinator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Component
public class GameplayWebSocketHandler extends TextWebSocketHandler {

    private final GameplaySessionCoordinator gameplaySessionCoordinator;
    private final ObjectMapper objectMapper;

    public GameplayWebSocketHandler(
            GameplaySessionCoordinator gameplaySessionCoordinator,
            ObjectMapper objectMapper
    ) {
        this.gameplaySessionCoordinator = gameplaySessionCoordinator;
        this.objectMapper = objectMapper;
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        Optional<UUID> sessionId = gameplaySessionId(session);
        if (sessionId.isEmpty() || !gameplaySessionCoordinator.attachConnection(sessionId.get(), session.getId())) {
            session.close(CloseStatus.POLICY_VIOLATION);
            return;
        }

        Optional<AuthoritativeMovementState> initialState = gameplaySessionCoordinator.currentState(sessionId.get());
        if (initialState.isEmpty()) {
            session.close(CloseStatus.SERVER_ERROR);
            return;
        }

        sendState(session, "position", initialState.get());
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        Optional<UUID> sessionId = gameplaySessionId(session);
        if (sessionId.isEmpty()) {
            session.close(CloseStatus.POLICY_VIOLATION);
            return;
        }

        MovementIntentMessage intent = readIntent(message);
        MovementDirection direction = intent.direction() == null ? MovementDirection.NONE : intent.direction();
        Instant inputTime = intent.inputTime() == null ? Instant.now() : intent.inputTime();

        Optional<AuthoritativeMovementState> updatedState = gameplaySessionCoordinator.applyIntent(
                sessionId.get(),
                inputTime,
                direction
        );
        if (updatedState.isEmpty()) {
            session.close(CloseStatus.POLICY_VIOLATION);
            return;
        }

        sendState(session, "position", updatedState.get());
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        gameplaySessionId(session).ifPresent(gameplaySessionCoordinator::closeSession);
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        gameplaySessionId(session).ifPresent(gameplaySessionCoordinator::closeSession);
        if (session.isOpen()) {
            session.close(CloseStatus.SERVER_ERROR);
        }
    }

    private MovementIntentMessage readIntent(TextMessage message) throws IOException {
        try {
            return objectMapper.readValue(message.getPayload(), MovementIntentMessage.class);
        } catch (JsonProcessingException exception) {
            throw new IOException("Invalid movement intent payload", exception);
        }
    }

    private void sendState(WebSocketSession session, String type, AuthoritativeMovementState state) throws IOException {
        GameplaySocketMessage socketMessage = new GameplaySocketMessage(type, state);
        session.sendMessage(new TextMessage(objectMapper.writeValueAsString(socketMessage)));
    }

    private Optional<UUID> gameplaySessionId(WebSocketSession session) {
        Object attribute = session.getAttributes().get(GameplayHandshakeInterceptor.GAMEPLAY_SESSION_ID_ATTRIBUTE);
        if (attribute instanceof UUID sessionId) {
            return Optional.of(sessionId);
        }

        return Optional.empty();
    }
}
