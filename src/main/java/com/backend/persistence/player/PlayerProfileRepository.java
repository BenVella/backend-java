package com.backend.persistence.player;

import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class PlayerProfileRepository {

    private final JdbcClient jdbcClient;

    public PlayerProfileRepository(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    public PlayerProfile create(String handle, String displayName) {
        UUID id = UUID.randomUUID();
        OffsetDateTime now = OffsetDateTime.now();

        jdbcClient.sql("""
                insert into player_profile (id, handle, display_name, created_at, updated_at)
                values (:id, :handle, :displayName, :createdAt, :updatedAt)
                """)
                .param("id", id)
                .param("handle", handle)
                .param("displayName", displayName)
                .param("createdAt", now)
                .param("updatedAt", now)
                .update();

        return new PlayerProfile(id, handle, displayName, now, now);
    }

    public Optional<PlayerProfile> findById(UUID id) {
        return jdbcClient.sql("""
                select id, handle, display_name, created_at, updated_at
                from player_profile
                where id = :id
                """)
                .param("id", id)
                .query(this::mapRow)
                .optional();
    }

    public Optional<PlayerProfile> findByHandle(String handle) {
        return jdbcClient.sql("""
                select id, handle, display_name, created_at, updated_at
                from player_profile
                where handle = :handle
                """)
                .param("handle", handle)
                .query(this::mapRow)
                .optional();
    }

    public List<PlayerProfile> findAll() {
        return jdbcClient.sql("""
                select id, handle, display_name, created_at, updated_at
                from player_profile
                order by created_at asc
                """)
                .query(this::mapRow)
                .list();
    }

    public long count() {
        Long count = jdbcClient.sql("select count(*) from player_profile")
                .query(Long.class)
                .single();
        return count == null ? 0L : count;
    }

    private PlayerProfile mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new PlayerProfile(
                rs.getObject("id", UUID.class),
                rs.getString("handle"),
                rs.getString("display_name"),
                rs.getObject("created_at", OffsetDateTime.class),
                rs.getObject("updated_at", OffsetDateTime.class)
        );
    }
}
