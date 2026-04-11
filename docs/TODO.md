# Game Backend Backlog

## Foundation
- [x] PR build/test workflow
- [x] Strip order prototype into a neutral game-backend baseline
- [x] OpenAPI-first contract generation
- Agent-enable project workflows

## Platform
- [x] Postgres schema and JdbcClient persistence baseline
- External OAuth/OpenID support
- Wire GitHub Actions secrets into delivery workflows
- Async leaderboard support

## Realtime
- WebSocket gameplay transport
- Broaden movement slice beyond self-only reconciliation
- Evaluate TLS 1.3 / QUIC for low-latency flows
- Split realtime systems into isolated modules
- Runtime redis capability
