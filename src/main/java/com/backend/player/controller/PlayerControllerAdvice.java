package com.backend.player.controller;

import com.backend.player.exception.PlayerDeletedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class PlayerControllerAdvice {

    @ExceptionHandler(PlayerDeletedException.class)
    public ResponseEntity<String> handlePlayerDeletedException(PlayerDeletedException ex) {
        return ResponseEntity.status(HttpStatus.GONE).body(ex.getMessage());
    }
}