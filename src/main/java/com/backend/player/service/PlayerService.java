package com.backend.player.service;

import com.backend.player.model.Player;
import com.backend.player.repository.PlayerRepository;
import org.springframework.stereotype.Service;

@Service
public class PlayerService {
    private final PlayerRepository playerRepository;

    public PlayerService(PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
    }

    public Player getOrCreatePlayer(String authId) {
        return playerRepository.findByAuthId(authId)
                .orElseGet(() -> {
                    Player newPlayer = new Player();
                    newPlayer.setAuthId(authId); // Use authId as authId
                    newPlayer.setCode(generateUniqueCodeWithValidation()); // Ensure unique code
                    newPlayer.setNickname(null); // Optional field, can be null
                    return playerRepository.save(newPlayer);
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