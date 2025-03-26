# Changelist

This runs with latest up top.  For whoever might be reviewing this, while this was spread over several days,
it rarely exceeded 15 mins at a time before an interruption stepped in.  Suffice to say this is not even close to half way done.

## 26/03/2025

- Introducing Resource Server.  Switched from Github to Google to enable issuer-uri checks for API usage
  - Github doesn't provide an issuer-uri and as long as we're given a token of sorts we're good to go for an API only backend
- Provided an index page where login can be done and OAuth functionality confirmed working.
  - Won't really be pushing on the webview much though.

## 25/03/2025

- Introduced OAuth2 spring boot, first time using this. Github
  - Seems simple enough to setup but lots of hidden configuration that's worth exploring
- Binned the JWT Auth...
  - Would be more interesting to try a modern integration with OAuth2 with potential SSO support

## 24/03/2025

- Realized Docker Compose gradlew distro downloads can be avoided with spring-boot-docker-compose plugin
    - Basically just hit the play button without messing too much with IntelliJ Configs.  The compose file gets auto used.
- Messed around with some docker setup and docker compose

## 22/03/2025

- Some basic Ordering API logic
- Introduced BezKoder's JWT auth

## 21/03/2025

- Spring Boot Java Project Skeleton