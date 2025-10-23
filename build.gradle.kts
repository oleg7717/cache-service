import org.gradle.api.tasks.testing.logging.TestExceptionFormat
import org.gradle.api.tasks.testing.logging.TestLogEvent

plugins {
    application
    id("org.springframework.boot") version "3.1.4"
    id("io.spring.dependency-management") version "1.1.3"
    id("com.github.ben-manes.versions") version "0.49.0"
}

group = "ru.interprocom.axioma"

version = "1.0"

application { mainClass.set("ru.interprocom.axioma.cache.Application") }

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.mapstruct:mapstruct:1.5.5.Final")
    compileOnly("org.projectlombok:lombok:1.18.30")
    annotationProcessor("org.projectlombok:lombok:1.18.30")
    annotationProcessor("org.mapstruct:mapstruct-processor:1.5.5.Final")
    implementation("org.springframework.boot:spring-boot-starter")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-devtools")
    implementation("org.postgresql:postgresql:42.7.7")
    implementation("org.redisson:redisson:3.50.0") {
        exclude(group = "io.netty", module = "netty-common")
        exclude(group = "io.netty", module = "netty-handler")
        exclude(group = "io.netty", module = "netty-codec")
    }
    implementation("io.netty:netty-common:4.2.7.Final")
    implementation("io.netty:netty-handler:4.2.7.Final")
    implementation("io.netty:netty-codec:4.2.7.Final")
    implementation("net.javacrumbs.shedlock:shedlock-spring:6.9.0")
    implementation("net.javacrumbs.shedlock:shedlock-provider-redis-spring:6.9.0")
    implementation("org.springframework.boot:spring-boot-starter-data-redis:3.5.3") {
        exclude(group = "io.netty", module = "netty-handler")
    }
    implementation("org.openapitools:jackson-databind-nullable:0.2.6")
    implementation("org.instancio:instancio-junit:3.3.1")

    runtimeOnly("com.h2database:h2")

//    implementation("com.github.ben-manes.caffeine:caffeine:3.2.1")
//    implementation("org.springframework.boot:spring-boot-starter-cache:3.5.3")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("net.datafaker:datafaker:2.0.2")
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter:5.10.0")
    testImplementation("net.javacrumbs.json-unit:json-unit-assertj:3.2.2")
    testImplementation("com.h2database:h2:2.4.240")
}

tasks.test {
    useJUnitPlatform()
    testLogging {
        exceptionFormat = TestExceptionFormat.FULL
        events = mutableSetOf(TestLogEvent.FAILED, TestLogEvent.PASSED, TestLogEvent.SKIPPED)
        showStandardStreams = true
    }
}
