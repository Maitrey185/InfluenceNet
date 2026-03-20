plugins {
    id("java")
    id("org.springframework.boot") version "3.3.5"
    id("io.spring.dependency-management") version "1.1.7"
}

group = "com.project"
version = "0.0.1-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {

    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-data-neo4j")
    implementation("org.springframework.boot:spring-boot-starter-aop")

    // Kafka
    implementation("org.springframework.kafka:spring-kafka")

    implementation(project(":notification-contract"))
    implementation(project(":influencerPost-contract"))
    implementation("com.fasterxml.jackson.core:jackson-databind")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310")

    // PostgreSQL Driver
    runtimeOnly("org.postgresql:postgresql")

    // Lombok
    compileOnly("org.projectlombok:lombok")
    annotationProcessor("org.projectlombok:lombok")

    // Swagger
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.5.0")


    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.test {
    useJUnitPlatform()
}