package com.backend.gameplay.transport;

import com.backend.gameplay.session.GameplaySessionCoordinator;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketSession;

import java.util.Map;
import java.util.UUID;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class GameplayWebSocketHandlerTest {

    @Test
    void closesOnlyTheAttachedConnectionWhenSocketEnds() {
        GameplaySessionCoordinator gameplaySessionCoordinator = mock(GameplaySessionCoordinator.class);
        GameplayWebSocketHandler handler = new GameplayWebSocketHandler(gameplaySessionCoordinator, new ObjectMapper());
        WebSocketSession session = mock(WebSocketSession.class);
        UUID sessionId = UUID.randomUUID();

        when(session.getId()).thenReturn("active-connection");
        when(session.getAttributes()).thenReturn(Map.of(
                GameplayHandshakeInterceptor.GAMEPLAY_SESSION_ID_ATTRIBUTE,
                sessionId
        ));

        handler.afterConnectionClosed(session, CloseStatus.NORMAL);

        verify(gameplaySessionCoordinator).closeConnection(sessionId, "active-connection");
    }
}
