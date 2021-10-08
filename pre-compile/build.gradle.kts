import org.gradle.api.tasks.testing.logging.TestExceptionFormat

plugins {
    kotlin("jvm") version "1.3.60"
}

repositories {
    jcenter()
}

dependencies {
    compile(kotlin("stdlib"))
    
    testImplementation("junit:junit:4.12")
    testImplementation(kotlin("test-junit"))
    testImplementation("org.junit.platform:junit-platform-console-standalone:1.8.1")
}

tasks.withType<Test> {
    testLogging {
        exceptionFormat = TestExceptionFormat.FULL
        events("passed", "failed", "skipped")
    }
}
