plugins {
    kotlin("jvm") version "2.1.10"
}

group = "com.sventripikal"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
    testImplementation ("com.github.stefanbirkner:system-lambda:1.2.0") // system-out tests

}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(21)
}