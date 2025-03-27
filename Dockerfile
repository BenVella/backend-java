FROM eclipse-temurin:21-jre
ENV APP_HOME=/app/
WORKDIR $APP_HOME
COPY build/libs/order-taking-api-0.0.1-SNAPSHOT.jar .
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "$APP_HOME/order-taking-api-0.0.1-SNAPSHOT.jar"]
