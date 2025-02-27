plugins {
	java
	id("org.springframework.boot") version "3.4.2"
	id("io.spring.dependency-management") version "1.1.7"
}

springBoot {
	buildInfo()
}

group = "ru.innotech"
version = "0.0.1-SNAPSHOT"

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(17)
	}
}

configurations {
	compileOnly {
		extendsFrom(configurations.annotationProcessor.get())
	}
}

repositories {
	mavenCentral()
}

dependencies {
	/**
	 * Spring-boot-starters
	 */
	implementation("org.springframework.boot:spring-boot-starter")
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.springframework.boot:spring-boot-starter-validation")
	implementation ("org.springframework.boot:spring-boot-starter-mail")

	/**
	 * DataBase
	 */
	implementation("org.liquibase:liquibase-core")
	runtimeOnly("org.postgresql:postgresql")

	/**
	 * Utils
	 */
	annotationProcessor("org.projectlombok:lombok")
	annotationProcessor("org.mapstruct:mapstruct-processor:1.5.3.Final")

	implementation("org.projectlombok:lombok")
	implementation("org.mapstruct:mapstruct:1.5.3.Final")

	implementation("org.springframework.kafka:spring-kafka")

	implementation("org.springframework.retry:spring-retry:2.0.11")


	/**
	 * Tests
	 */
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")
	testImplementation("org.springframework.boot:spring-boot-starter-test")

	testImplementation("org.mockito:mockito-core:5.1.0")
	testImplementation("org.mockito:mockito-junit-jupiter:5.1.0")

	testImplementation("org.springframework.kafka:spring-kafka-test")
}

tasks.named<org.springframework.boot.gradle.tasks.bundling.BootJar>("bootJar") {
	archiveFileName.set("innotech-0.0.1-SNAPSHOT.jar")
	archiveVersion.set("0.0.1-SNAPSHOT")
}

tasks.withType<Test> {
	useJUnitPlatform()
	jvmArgs("-XX:+EnableDynamicAgentLoading")
}
