FROM eclipse-temurin:21-jre
ENV APP_HOME=/app/
WORKDIR $APP_HOME
COPY build/libs/order-taking-api-0.0.1-SNAPSHOT.jar app.jar
COPY ./docker-compose.yml /app/docker-compose.yml
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
