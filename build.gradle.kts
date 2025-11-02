plugins {
	java
	id("org.springframework.boot") version "3.4.4"
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
//	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation("org.springframework.boot:spring-boot-starter-data-mongodb")
	implementation("org.springframework.boot:spring-boot-starter-data-redis")
	implementation("org.springframework.boot:spring-boot-starter-data-neo4j")
	implementation("org.springframework.boot:spring-boot-starter-data-elasticsearch")
	implementation("org.springframework.boot:spring-boot-starter-validation")
	implementation("org.springframework.boot:spring-boot-starter-actuator")
	implementation("org.springframework.boot:spring-boot-starter-cache")
//    implementation("org.springframework.boot:spring-boot-starter-security")
//    implementation("org.springframework.boot:spring-boot-starter-oauth2-resource-server")
//    implementation("org.springframework.boot:spring-boot-starter-oauth2-client")
    implementation("org.springframework.boot:spring-boot-starter-mail")
    // Spring Cloud Gateway
//    implementation("org.springframework.cloud:spring-cloud-starter-gateway")

    // WebFlux
    implementation("org.springframework.boot:spring-boot-starter-webflux")

    // Kafka
//	implementation("org.springframework.kafka:spring-kafka")
	
	// PostgreSQL Driver
	runtimeOnly("org.postgresql:postgresql")
	
	// Flyway for database migrations
	implementation("org.flywaydb:flyway-core")
	implementation("org.flywaydb:flyway-database-postgresql")
	
	// Redis Lettuce (default for Spring Data Redis)
	implementation("io.lettuce:lettuce-core")
	
	// JSON Processing
	implementation("com.fasterxml.jackson.core:jackson-databind")
	implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310")
	
	// Lombok (optional but recommended)
	compileOnly("org.projectlombok:lombok")
	annotationProcessor("org.projectlombok:lombok")
	
	// Micrometer for metrics (Prometheus)
	implementation("io.micrometer:micrometer-registry-prometheus")
	implementation("io.micrometer:micrometer-tracing-bridge-brave")
	
	// Zipkin for distributed tracing
	implementation("io.zipkin.reporter2:zipkin-reporter-brave")
	
	// AWS SDK for S3 (MinIO compatible)
	implementation("software.amazon.awssdk:s3:2.20.26")
	
	// Apache Commons
    implementation("org.apache.commons:commons-lang3:3.14.0")

    // Keycloak Admin Client
//    implementation("org.keycloak:keycloak-admin-client:23.0.0")

    // OpenAPI/Swagger Documentation
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.2.0")
	
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

// Fix for Windows long classpath issue
tasks.withType<JavaExec> {
	// Use manifest classpath to avoid long command line
	classpath = files()
	doFirst {
		val manifestClasspath = project.configurations.runtimeClasspath.get().files.joinToString(" ") { it.name }
		jvmArgs("-Dloader.path=${project.configurations.runtimeClasspath.get().asPath}")
	}
}

// Alternative: Use ProGuard JAR for shorter classpath
tasks.named<org.springframework.boot.gradle.tasks.run.BootRun>("bootRun") {
	// Shorten classpath by using manifest
	systemProperty("spring.output.ansi.enabled", "always")
}

dependencyManagement {
    imports {
        mavenBom("org.springframework.cloud:spring-cloud-dependencies:2024.0.0")
    }
}
