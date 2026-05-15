package com.backend.gameplay.transport;

import com.backend.gameplay.session.GameplaySessionCoordinator;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Component
public class GameplayHandshakeInterceptor implements HandshakeInterceptor {

    static final String GAMEPLAY_SESSION_ID_ATTRIBUTE = "gameplaySessionId";

    private final GameplaySessionCoordinator gameplaySessionCoordinator;

    public GameplayHandshakeInterceptor(GameplaySessionCoordinator gameplaySessionCoordinator) {
        this.gameplaySessionCoordinator = gameplaySessionCoordinator;
    }

    @Override
    public boolean beforeHandshake(
            ServerHttpRequest request,
            ServerHttpResponse response,
            WebSocketHandler wsHandler,
            Map<String, Object> attributes
    ) {
        if (!(request instanceof ServletServerHttpRequest servletRequest)) {
            return false;
        }

        String sessionIdParam = servletRequest.getServletRequest().getParameter("sessionId");
        String tokenParam = servletRequest.getServletRequest().getParameter("token");

        Optional<UUID> sessionId = parseUuid(sessionIdParam);
        if (sessionId.isEmpty() || tokenParam == null || tokenParam.isBlank()) {
            return false;
        }

        return gameplaySessionCoordinator.validateConnection(sessionId.get(), tokenParam)
                .map(ticket -> {
                    attributes.put(GAMEPLAY_SESSION_ID_ATTRIBUTE, ticket.sessionId());
                    return true;
                })
                .orElse(false);
    }

    @Override
    public void afterHandshake(
            ServerHttpRequest request,
            ServerHttpResponse response,
            WebSocketHandler wsHandler,
            Exception exception
    ) {
        // no-op
    }

    private Optional<UUID> parseUuid(String candidate) {
        if (candidate == null || candidate.isBlank()) {
            return Optional.empty();
        }

        try {
            return Optional.of(UUID.fromString(candidate));
        } catch (IllegalArgumentException ignored) {
            return Optional.empty();
        }
    }
}
