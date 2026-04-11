package com.backend.gameplay.config;

import com.backend.gameplay.movement.GameplayMovementProperties;
import com.backend.gameplay.session.GameplaySessionProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Clock;

@Configuration
@EnableConfigurationProperties({
        GameplaySessionProperties.class,
        GameplayMovementProperties.class
})
public class GameplayConfig {

    @Bean
    public Clock systemClock() {
        return Clock.systemUTC();
    }
}
