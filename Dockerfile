#Build stage
FROM gradle:latest AS build
ENV APP_HOME=/app/
WORKDIR $APP_HOME
COPY . .
RUN chmod +x $APP_HOME/scripts/build.sh

# Package stage
FROM eclipse-temurin:21-jre
ENV APP_HOME=/app/
WORKDIR $APP_HOME
COPY --from=build /app/build/libs/order-taking-api-0.0.1-SNAPSHOT.jar .
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/order-taking-api-0.0.1-SNAPSHOT.jar"]
