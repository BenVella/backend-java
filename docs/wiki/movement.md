# Movement Slice

## Goal

Introduce the first minimal realtime gameplay slice without changing the repository's role as a Spring Boot control plane.

Current scope:
- authenticated gameplay session issuance over HTTP
- local in-memory authoritative movement runtime
- raw WebSocket transport for movement intent only
- coarse durable player location persistence for reconnect and logout recovery

Out of scope for this slice:
- combat, inventory, replication beyond self-state, matchmaking
- UDP, Redis, multi-node coordination, distributed interest management
- per-frame database writes

## Control Plane Vs Realtime Boundary

Control plane responsibilities in this repository:
- validate bearer tokens with the existing Keycloak-backed resource server
- issue short-lived gameplay session tokens from authenticated HTTP requests
- bootstrap player identity and recover last durable coarse location
- persist coarse location when a gameplay session is closed or disconnected

Realtime boundary responsibilities in this slice:
- accept movement intent over WebSocket
- maintain active movement state in memory
- compute authoritative position on the server
- emit authoritative position snapshots back to the same client

The transport layer is intentionally thin. WebSocket code validates session access and forwards parsed intent into gameplay services, but it does not own movement rules.

## Authoritative Intent Model

The client sends intent, not absolute position:
- session id is implicit in the WebSocket connection
- each message carries an input timestamp and one 8-direction movement intent
- the server advances from its last authoritative state using elapsed server time and configured movement speed
- the server accepts only the latest input timestamp for a session and ignores older intent updates

Why not send absolute position:
- client positions are easy to spoof
- server-side integration gives one source of truth for reconciliation
- later physics, collision, stamina, or map constraints can hook into the same authority layer without changing the transport contract

Current movement model shortcuts:
- timestamp-driven simulation instead of a dedicated fixed tick loop
- one active session per player inside a single JVM
- only the moving player receives authoritative state updates

## Persistence Strategy

Persisted durably:
- `player_profile.external_subject` as the current auth-to-player bridge
- `player_location` with `player_id`, `zone_id`, `position_x`, `position_y`, and `updated_at`

Kept in memory only:
- issued gameplay session tokens
- active authoritative movement state
- active WebSocket connection attachment

Persistence boundaries for this slice:
- gameplay session replacement for the same player
- WebSocket disconnect or transport error

Not persisted on every update:
- movement intent messages
- intermediate authoritative movement frames

This keeps PostgreSQL on the control-plane side of the design and avoids turning movement into a write-amplified hot path.

## Local Flow

1. Client calls `POST /api/gameplay/sessions` with a normal bearer token.
2. Server resolves or creates a local player profile and loads the last durable coarse location.
3. Server returns a short-lived session id, session token, WebSocket path, and initial authoritative state.
4. Client connects to `/ws/gameplay?sessionId=...&token=...`.
5. Client sends movement intent messages like:

```json
{"inputTime":"2026-04-11T08:00:00Z","direction":"NORTH_EAST"}
```

6. Server replies with authoritative position snapshots for reconciliation.
7. On disconnect, the latest coarse location is persisted.

## Replace Later

Expected replacement points as the realtime stack grows:
- raw WebSocket handshake/session-token flow can move behind a dedicated gateway or realtime service
- in-memory session and movement stores can move to dedicated process state management
- timestamp-driven movement integration can become a fixed-step simulation loop
- self-only outbound updates can evolve into area-of-interest replication
- coarse persistence on disconnect can become explicit checkpoints or handoff events
