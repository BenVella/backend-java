package com.backend.player.service;

import com.backend.player.exception.PlayerDeletedException;
import com.backend.player.model.Player;
import com.backend.player.repository.PlayerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.function.Function;
import java.util.function.Supplier;

@Service
public class PlayerService {
    private static final Logger log = LoggerFactory.getLogger(PlayerService.class);
    private final PlayerRepository playerRepository;

    public PlayerService(PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
    }

    @PreAuthorize("isAuthenticated()")
    public Player getOrCreatePlayer(String authId) {
        return playerRepository.findByAuthId(authId)
                .map(processExistingPlayer(authId))
                .orElseGet(processNewPlayer(authId));
    }

    private Supplier<Player> processNewPlayer(String authId) {
        return () -> {
            Player newPlayer = new Player();
            newPlayer.setAuthId(authId); // Use authId as authId
            newPlayer.setCode(generateUniqueCodeWithValidation()); // Ensure unique code
            newPlayer.setNickname(null); // Optional field, can be null
            var player = playerRepository.save(newPlayer);
            log.info("Creating new player for authId: {} with player id: {}", authId, player.getId());
            return player;
        };
    }

    private static Function<Player, Player> processExistingPlayer(String authId) {
        return player -> {
            if (player.isDeleted()) {
                String message = "Player with authId " + authId + " is deleted.";
                log.error(message);
                throw new PlayerDeletedException(message);
            }

            log.info("Player found for authId: {} with player id: {}", authId, player.getId());
            return player;
        };
    }

    @PreAuthorize("isAuthenticated()")
    public void deletePlayer(String authId) {
        playerRepository.findByAuthIdAndDeletedFalse(authId).ifPresent(player -> {
            player.setDeleted(true);
            playerRepository.save(player);
            log.info("Player marked as deleted for authId: {}", authId);
        });
    }

    private String generateUniqueCodeWithValidation() {
        String code;
        do {
            code = generateUniqueCode();
        } while (playerRepository.findByCode(code).isPresent());
        return code;
    }

    private String generateUniqueCode() {
        String characters = "ABCDEFGHJKLMNPQRSTUVWXYZabcdefghijkmnopqrstuvwxyz23456789"; // Excludes ambiguous characters
        StringBuilder code = new StringBuilder();
        for (int i = 0; i < 8; i++) {
            int index = (int) (Math.random() * characters.length());
            code.append(characters.charAt(index));
        }
        return code.toString();
    }
}