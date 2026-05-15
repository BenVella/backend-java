package com.backend.gameplay.session;

import com.backend.persistence.player.PlayerProfile;
import com.backend.persistence.player.PlayerProfileRepository;
import org.springframework.stereotype.Service;

@Service
public class PlayerIdentityService {

    private static final int MAX_HANDLE_LENGTH = 32;
    private static final int MAX_DISPLAY_NAME_LENGTH = 64;

    private final PlayerProfileRepository playerProfileRepository;

    public PlayerIdentityService(PlayerProfileRepository playerProfileRepository) {
        this.playerProfileRepository = playerProfileRepository;
    }

    public PlayerIdentity resolvePlayer(String externalSubject, String preferredHandle) {
        String normalizedHandle = normalize(preferredHandle, MAX_HANDLE_LENGTH, "player");
        String displayName = normalize(preferredHandle, MAX_DISPLAY_NAME_LENGTH, normalizedHandle);

        PlayerProfile profile = playerProfileRepository.findOrCreate(externalSubject, normalizedHandle, displayName);
        return new PlayerIdentity(profile.id(), profile.externalSubject(), profile.handle(), profile.displayName());
    }

    private String normalize(String candidate, int maxLength, String fallback) {
        if (candidate == null || candidate.isBlank()) {
            return fallback;
        }

        String trimmed = candidate.trim();
        if (trimmed.length() <= maxLength) {
            return trimmed;
        }

        return trimmed.substring(0, maxLength);
    }
}
