import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    kotlin("jvm") version "2.4.0"
    id("com.gradleup.shadow") version "9.4.2"
    application
}

repositories {
    mavenLocal()
    mavenCentral()
    maven("https://jitpack.io")
}

dependencies {
    val version = object {
        val moshi = "1.15.2"
        val kotest = "6.1.11"
    }

    implementation("com.xenomachina:kotlin-argparser:2.0.7")

    implementation("com.squareup.moshi:moshi:${version.moshi}")
    implementation("com.squareup.moshi:moshi-kotlin:${version.moshi}")

    implementation("org.dom4j:dom4j:2.2.0")

    testImplementation("org.junit.jupiter:junit-jupiter:6.1.0")
    testImplementation("io.kotest:kotest-assertions-core:${version.kotest}")
    testImplementation("io.kotest:kotest-framework-engine:${version.kotest}")
    testImplementation("io.kotest:kotest-runner-junit5:${version.kotest}")
}

tasks.withType<Test> {
    useJUnitPlatform()
}

application {
    mainClass.set("exercism.kotlin.autotests.runner.MainKt")
}

java {
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
}

kotlin {
    jvmToolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
}

tasks.withType<ShadowJar> {
    archiveFileName.set("autotest-runner.jar")
}
