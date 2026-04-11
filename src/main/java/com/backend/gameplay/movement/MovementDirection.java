package com.backend.gameplay.movement;

public enum MovementDirection {
    NONE(0.0, 0.0),
    NORTH(0.0, 1.0),
    NORTH_EAST(0.7071067811865476, 0.7071067811865476),
    EAST(1.0, 0.0),
    SOUTH_EAST(0.7071067811865476, -0.7071067811865476),
    SOUTH(0.0, -1.0),
    SOUTH_WEST(-0.7071067811865476, -0.7071067811865476),
    WEST(-1.0, 0.0),
    NORTH_WEST(-0.7071067811865476, 0.7071067811865476);

    private final double xComponent;
    private final double yComponent;

    MovementDirection(double xComponent, double yComponent) {
        this.xComponent = xComponent;
        this.yComponent = yComponent;
    }

    public double xComponent() {
        return xComponent;
    }

    public double yComponent() {
        return yComponent;
    }
}
