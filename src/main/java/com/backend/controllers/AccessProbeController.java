package com.backend.controllers;

import com.backend.api.AccessResponse;
import com.backend.api.ApiErrorResponse;
import com.backend.api.StatusResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "Access", description = "Access probe endpoints for public and role-protected verification.")
public class AccessProbeController {

    @GetMapping("/api/ping")
    @Operation(summary = "Public health ping", security = {})
    @ApiResponse(
            responseCode = "200",
            description = "Service is available",
            content = @Content(schema = @Schema(implementation = StatusResponse.class))
    )
    public ResponseEntity<StatusResponse> ping() {
        return ResponseEntity.ok(new StatusResponse("ok"));
    }

    @GetMapping("/api/access/public")
    @Operation(summary = "Public access probe", security = {})
    @ApiResponse(
            responseCode = "200",
            description = "Public endpoint is reachable",
            content = @Content(schema = @Schema(implementation = AccessResponse.class))
    )
    public ResponseEntity<AccessResponse> publicAccess() {
        return ResponseEntity.ok(new AccessResponse("public"));
    }

    @GetMapping("/api/access/user")
    @Operation(
            summary = "User access probe",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "User or admin access granted",
                    content = @Content(schema = @Schema(implementation = AccessResponse.class))
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Missing or invalid bearer token",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Token valid but missing required role",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))
            )
    })
    public ResponseEntity<AccessResponse> userAccess() {
        return ResponseEntity.ok(new AccessResponse("user"));
    }

    @GetMapping("/api/access/admin")
    @Operation(
            summary = "Admin access probe",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Admin access granted",
                    content = @Content(schema = @Schema(implementation = AccessResponse.class))
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Missing or invalid bearer token",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Token valid but missing required role",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))
            )
    })
    public ResponseEntity<AccessResponse> adminAccess() {
        return ResponseEntity.ok(new AccessResponse("admin"));
    }
}
