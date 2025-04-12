plugins {
    java
    id("org.springframework.boot") version "3.4.4"
    id("io.spring.dependency-management") version "1.1.7"
}

group = "com.backend"
version = "0.0.1-SNAPSHOT"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

repositories {
    mavenCentral()
}

dependencies {
    // Blocking servlet-based synchronous communication.
    // We can switch `web` to `webflux` for reactive / async processing
    implementation("org.springframework.boot:spring-boot-starter-web")

    // JWT Decoding Resource Server (not via introspection, to prevent latency)
    implementation("org.springframework.boot:spring-boot-starter-oauth2-resource-server")
    implementation("com.c4-soft.springaddons:spring-addons-starter-oidc:8.1.9")

    // Data
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-amqp")

    // Testing
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.security:spring-security-test")
}

tasks.withType<Test> {
    useJUnitPlatform()
}
