package com.backend.player.controller;

import com.backend.player.model.Player;
import com.backend.player.service.PlayerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.DeleteMapping;
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
        String authId = getAuthId(jwt);

        log.info("GetPlayer - Extracted authId: {}", authId);
        Player result = playerService.getOrCreatePlayer(authId);
        log.info("GetPlayer - Player retrieved or created: {}", result);
        return result;
    }

    @DeleteMapping
    public ResponseEntity<Void> deletePlayer(@AuthenticationPrincipal Jwt jwt) {
        String authId = getAuthId(jwt);

        log.info("DeletePlayer - Extracted authId: {}", authId);
        playerService.deletePlayer(authId);
        log.info("DeletePlayer - Player marked as deleted for authId: {}", authId);

        return ResponseEntity.noContent().build(); // Return 204 No Content
    }

    private static String getAuthId(Jwt jwt) {
        return jwt.getSubject();
    }
}
