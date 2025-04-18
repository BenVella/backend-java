# Changelist

This runs with latest up top.  For whoever might be reviewing this, while this was spread over several days,
it rarely exceeded 15 mins at a time before an interruption stepped in.  Suffice to say this is not even close to half way done.

## 18/04/2025

- Introducing the `com.backend.player` package
  - Added a player entity and repository
  - Plan is to fetch subject from provided jwt after successful authentication to use as client id
- Faffing about with having two separate DBs, one for keycloak and one for the app

## 04/04/2025

- Confirmed a standalone working version of keycloak with securityfilterchain against issuer jwt decoding
  - This was tested without the spring-addons by Ch4mpy but using his own examples
- The plan is to use his spring addons alongside confirmed working setup of keycloak.
- Updated some readme notes on this
- Updated docker compose to load the baeldung-keycloak preconfigured endpoint
  - I had to manually amend keycloak to accept direct access, which is not reflected in that json file setup.
  - That setup is also overkill and over-done.  A minimal version would be best, ideally with comments (Doubt it would let us get away with comments in json however)

## 27/03/2025

- Still messing with auths, Google isn't a good fit either since their JWT are opaque and require introspection.
- The work has ended up just becoming auth prototyping with Spring Boot I suppose, Keycloak looks like the ticket but with what little time I have left it looks like this will take a fair bit longer to sort out than initially expected.
- Maybe bypassing auth entirely would have been the smarter choice, but it seemed like a more interesting challenge to tackle for having inbuilt authentication that can come through with docker compose.
- The API should essentially have payload -> request and response objects, determining everything and introducing Open API Spec 3.0 to cover and converge on these details.
  - `springdoc-openapi-starter-webmvc-ui` would have probably been the ticket in this case

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
  - (Future me: Later realised that the plugin ain't too consistent and there were bug reports... I skipped it)
- Messed around with some docker setup and docker compose

## 22/03/2025

- Some basic Ordering API logic
- Introduced BezKoder's JWT auth

## 21/03/2025

- Spring Boot Java Project Skeleton