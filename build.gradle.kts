import org.openapitools.generator.gradle.plugin.tasks.GenerateTask

plugins {
    java
    id("org.springframework.boot") version "3.4.4"
    id("io.spring.dependency-management") version "1.1.7"
    id("org.openapi.generator") version "7.14.0"
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

val generatedOpenApiDir = layout.buildDirectory.dir("generated/openapi")

sourceSets {
    named("main") {
        java.srcDir(generatedOpenApiDir.map { it.dir("src/main/java") })
    }
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.boot:spring-boot-starter-oauth2-resource-server")

    // API documentation
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.8.9")
    implementation("io.swagger.core.v3:swagger-annotations-jakarta:2.2.30")

    // Data
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-amqp")

    // Testing
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.security:spring-security-test")
}

tasks.register<GenerateTask>("generateOpenApiServer") {
    generatorName.set("spring")
    inputSpec.set("$projectDir/src/main/resources/openapi/order-taking-api.yaml")
    outputDir.set(generatedOpenApiDir.get().asFile.absolutePath)
    apiPackage.set("com.backend.openapi.api")
    modelPackage.set("com.backend.openapi.model")
    configOptions.set(
        mapOf(
            "interfaceOnly" to "true",
            "useSpringBoot3" to "true",
            "useJakartaEe" to "true",
            "documentationProvider" to "springdoc",
            "openApiNullable" to "false",
            "performBeanValidation" to "true",
            "useTags" to "true"
        )
    )
}

tasks.withType<JavaCompile> {
    dependsOn(tasks.named("generateOpenApiServer"))
}

tasks.withType<Test> {
    useJUnitPlatform()
}
