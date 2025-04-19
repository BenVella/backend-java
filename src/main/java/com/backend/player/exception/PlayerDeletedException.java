package com.backend.player.exception;

public class PlayerDeletedException extends RuntimeException {
    public PlayerDeletedException(String message) {
        super(message);
    }
}