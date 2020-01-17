plugins {
    kotlin("jvm") version "1.3.60"
}

repositories {
    mavenLocal()
    jcenter()

}

dependencies {
    implementation(kotlin("stdlib-jdk8"))

    implementation("com.xenomachina:kotlin-argparser:2.0.7")
}
