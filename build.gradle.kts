import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version Versions.kotlin
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    //implementation(Deps.kotlin_coroutines)

    //implementation(Deps.clikt)
    implementation(Deps.moshi_kotlin)

    implementation(Deps.jupiter_engine)
    implementation(Deps.jupiter_launcher)
    implementation(Deps.junit4)

    //testImplementation(Deps.kotlin_test)
}

allprojects {
    group = "io.exercism.${rootProject.name}"
    version = "0.1-SNAPSHOT"

    repositories {
        jcenter()
    }

    tasks.withType<KotlinCompile> {
        kotlinOptions.jvmTarget = "1.8"
    }
}

tasks.withType<Test>() {
    useJUnitPlatform()
}
