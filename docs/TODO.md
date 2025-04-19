# Todo

## Up next

- restore deleted player profile
- tests for player package

## Authentication

- Complete resources server based token validation for API usage
  - JWT implementation we had relied on our own postgres and that could easily be phased out
- Still messing with auths, Google isn't a good fit either since their JWT are most likely opaque and require introspection, which is a lot more hassle than its wort, which is a lot more hassle than its worth.
  - The work has ended up just becoming auth prototyping with Spring Boot I suppose, Keycloak looks like the ticket but with what little time I have left it looks like this will take a fair bit longer to sort out than initially expected.
  - Maybe bypassing auth entirely would have been the smarter choice, but it seemed like a more interesting challenge to tackle for having inbuilt authentication that can come through with docker compose.

## API Flow

- The API should essentially have its own module `order` that's a dedicated package as `com.backend.order`
- Grouping operations would be in Controllers such as the OrderController which would expose client and admin facing endpoints
- General manipulation of code takes place under the service package, each Controller having a matching service
- A dedicated 
- payload -> request and response objects, determining everything and introducing Open API Spec 3.0 to cover and converge on these details.
    - `springdoc-openapi-starter-webmvc-ui` would have probably been the ticket
