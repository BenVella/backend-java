package com.backend.persistence.player;

import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.OffsetDateTime;
import java.util.Optional;
import java.util.UUID;

@Repository
public class PlayerLocationRepository {

    private final JdbcClient jdbcClient;

    public PlayerLocationRepository(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    public Optional<PlayerLocation> findByPlayerId(UUID playerId) {
        return jdbcClient.sql("""
                select player_id, zone_id, position_x, position_y, updated_at
                from player_location
                where player_id = :playerId
                """)
                .param("playerId", playerId)
                .query(this::mapRow)
                .optional();
    }

    public PlayerLocation upsert(UUID playerId, String zoneId, double positionX, double positionY) {
        OffsetDateTime now = OffsetDateTime.now();

        jdbcClient.sql("""
                insert into player_location (player_id, zone_id, position_x, position_y, updated_at)
                values (:playerId, :zoneId, :positionX, :positionY, :updatedAt)
                on conflict (player_id) do update
                set zone_id = excluded.zone_id,
                    position_x = excluded.position_x,
                    position_y = excluded.position_y,
                    updated_at = excluded.updated_at
                """)
                .param("playerId", playerId)
                .param("zoneId", zoneId)
                .param("positionX", positionX)
                .param("positionY", positionY)
                .param("updatedAt", now)
                .update();

        return new PlayerLocation(playerId, zoneId, positionX, positionY, now);
    }

    private PlayerLocation mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new PlayerLocation(
                rs.getObject("player_id", UUID.class),
                rs.getString("zone_id"),
                rs.getDouble("position_x"),
                rs.getDouble("position_y"),
                rs.getObject("updated_at", OffsetDateTime.class)
        );
    }
}
