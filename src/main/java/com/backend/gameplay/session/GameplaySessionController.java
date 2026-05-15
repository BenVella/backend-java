package com.backend.gameplay.session;

import com.backend.api.ApiErrorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/gameplay/sessions")
@Tag(name = "Gameplay Sessions", description = "Authenticated control-plane endpoints for gameplay session issuance.")
public class GameplaySessionController {

    private final GameplaySessionCoordinator gameplaySessionCoordinator;

    public GameplaySessionController(GameplaySessionCoordinator gameplaySessionCoordinator) {
        this.gameplaySessionCoordinator = gameplaySessionCoordinator;
    }

    @PostMapping
    @Operation(
            summary = "Issue a short-lived gameplay session token",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Gameplay session issued",
                    content = @Content(schema = @Schema(implementation = GameplaySessionResponse.class))
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Missing or invalid bearer token",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))
            )
    })
    public ResponseEntity<GameplaySessionResponse> issueSession(@AuthenticationPrincipal Jwt jwt) {
        GameplaySessionResponse response = gameplaySessionCoordinator.issueSession(
                jwt.getSubject(),
                preferredHandle(jwt)
        );
        return ResponseEntity.ok(response);
    }

    private String preferredHandle(Jwt jwt) {
        String preferredUsername = jwt.getClaimAsString("preferred_username");
        if (preferredUsername != null && !preferredUsername.isBlank()) {
            return preferredUsername;
        }

        return jwt.getSubject();
    }
}
