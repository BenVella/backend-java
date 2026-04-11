package com.backend.gameplay.transport;

import com.backend.gameplay.session.GameplaySessionProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
public class GameplayWebSocketConfig implements WebSocketConfigurer {

    private final GameplayWebSocketHandler gameplayWebSocketHandler;
    private final GameplayHandshakeInterceptor gameplayHandshakeInterceptor;
    private final GameplaySessionProperties gameplaySessionProperties;

    public GameplayWebSocketConfig(
            GameplayWebSocketHandler gameplayWebSocketHandler,
            GameplayHandshakeInterceptor gameplayHandshakeInterceptor,
            GameplaySessionProperties gameplaySessionProperties
    ) {
        this.gameplayWebSocketHandler = gameplayWebSocketHandler;
        this.gameplayHandshakeInterceptor = gameplayHandshakeInterceptor;
        this.gameplaySessionProperties = gameplaySessionProperties;
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(gameplayWebSocketHandler, gameplaySessionProperties.getWebsocketPath())
                .addInterceptors(gameplayHandshakeInterceptor)
                .setAllowedOriginPatterns("*");
    }
}
