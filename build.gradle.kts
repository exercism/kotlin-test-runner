plugins {
    kotlin("jvm") version "1.3.60"
}

repositories {
    mavenLocal()
    jcenter()
    maven("https://jitpack.io")
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))

    implementation("com.xenomachina:kotlin-argparser:2.0.7")
    implementation("com.github.swordglowsblue:haven-json:1.1.2")
    implementation("org.dom4j:dom4j:2.1.1")
}
