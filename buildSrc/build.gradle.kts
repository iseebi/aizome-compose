plugins {
    kotlin("jvm") version "2.0.21"
}

repositories {
    mavenCentral()
}

kotlin {
    jvmToolchain(21)
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21)) }
}