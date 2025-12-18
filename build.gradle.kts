plugins {
    java
    id("org.springframework.boot") version "3.4.1"
    id("io.spring.dependency-management") version "1.1.7"
}

group = "com.project"
version = "0.0.1-SNAPSHOT"
description = "A unified platform for influencer growth + brand campaign management"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(17)
    }
}

repositories {
    mavenCentral()
}

dependencies {
    // Spring Boot Starters
    implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-data-mongodb")
	implementation("org.springframework.boot:spring-boot-starter-data-redis")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310")
//	implementation("org.springframework.boot:spring-boot-starter-data-neo4j")
//	implementation("org.springframework.boot:spring-boot-starter-data-elasticsearch")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.boot:spring-boot-starter-cache")
    implementation("org.springframework.boot:spring-boot-starter-security")

    implementation("io.jsonwebtoken:jjwt-api:0.11.5")
    runtimeOnly("io.jsonwebtoken:jjwt-impl:0.11.5")
    runtimeOnly("io.jsonwebtoken:jjwt-jackson:0.11.5") // for JSON parser
    // Kafka
	implementation("org.springframework.kafka:spring-kafka")


    // Apache Tika for language detection and content analysis
    implementation("org.apache.tika:tika-core:2.9.0")
    implementation("org.apache.tika:tika-langdetect:2.9.0")
    implementation("org.apache.tika:tika-langdetect-optimaize:2.9.0")

    // PostgreSQL Driver
    runtimeOnly("org.postgresql:postgresql")

    // Flyway for database migrations
//	implementation("org.flywaydb:flyway-core")
//	implementation("org.flywaydb:flyway-database-postgresql")
//
//	// Redis Lettuce (default for Spring Data Redis)
//	implementation("io.lettuce:lettuce-core")
//
    // JSON Processing
    implementation("com.fasterxml.jackson.core:jackson-databind")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310")

    // Lombok (optional but recommended)
    compileOnly("org.projectlombok:lombok")
    annotationProcessor("org.projectlombok:lombok")

    // Micrometer for metrics (Prometheus)
//	implementation("io.micrometer:micrometer-registry-prometheus")
//	implementation("io.micrometer:micrometer-tracing-bridge-brave")
//
//	// Zipkin for distributed tracing
//	implementation("io.zipkin.reporter2:zipkin-reporter-brave")
//
//	// AWS SDK for S3 (MinIO compatible)
//	implementation("software.amazon.awssdk:s3:2.20.26")

    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.3.0")
    implementation("org.springframework.boot:spring-boot-starter-webflux")

    // Apache Commons
    implementation("org.apache.commons:commons-lang3")

    // Testing
    testImplementation("org.springframework.boot:spring-boot-starter-test")
//	testImplementation("org.springframework.kafka:spring-kafka-test")
    testImplementation("org.testcontainers:testcontainers:1.19.3")
    testImplementation("org.testcontainers:postgresql:1.19.3")
    testImplementation("org.testcontainers:mongodb:1.19.3")
//	testImplementation("org.testcontainers:kafka:1.19.3")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.withType<Test> {
    useJUnitPlatform()
}