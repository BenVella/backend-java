package com.backend.gameplay.movement;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app.gameplay.movement")
public class GameplayMovementProperties {

    private double speedUnitsPerSecond = 4.0;
    private String defaultZoneId = "starter-zone";

    public double getSpeedUnitsPerSecond() {
        return speedUnitsPerSecond;
    }

    public void setSpeedUnitsPerSecond(double speedUnitsPerSecond) {
        this.speedUnitsPerSecond = speedUnitsPerSecond;
    }

    public String getDefaultZoneId() {
        return defaultZoneId;
    }

    public void setDefaultZoneId(String defaultZoneId) {
        this.defaultZoneId = defaultZoneId;
    }
}
