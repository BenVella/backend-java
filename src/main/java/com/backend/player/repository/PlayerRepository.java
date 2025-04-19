package com.backend.player.repository;

import com.backend.player.model.Player;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PlayerRepository extends JpaRepository<Player, Long> {
    Optional<Player> findByCode(String code);
    Optional<Player> findByAuthId(String authId);
    Optional<Player> findByAuthIdAndDeletedFalse(String authId);
    Optional<Player> findByCodeAndDeletedFalse(String code);
}