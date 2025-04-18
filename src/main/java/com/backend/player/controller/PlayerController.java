package com.backend.player.controller;

import com.backend.player.model.Player;
import com.backend.player.service.PlayerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/player")
public class PlayerController {
    private static final Logger log = LoggerFactory.getLogger(PlayerController.class);
    private final PlayerService playerService;

    public PlayerController(PlayerService playerService) {
        this.playerService = playerService;
    }

    @GetMapping
    public Player getPlayer(@AuthenticationPrincipal Jwt jwt) {
        log.info("GetPlayer - Endpoint called with JWT: {}", jwt);
        String username = jwt.getSubject(); // Extract subject from JWT

        log.info("GetPlayer - Extracted username: {}", username);
        return playerService.getOrCreatePlayer(username);
    }
}