import java.text.SimpleDateFormat
import java.util.*

plugins {
    id("java")
    id("idea")
    id("io.papermc.paperweight.userdev") version("1.7.2")
}

group = "net.njsharpe"
version = "${SimpleDateFormat("yyyyMMdd").format(Date())}-1.0.0"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
    withJavadocJar()
    withSourcesJar()
}

repositories {
    mavenCentral()
}

dependencies {
    paperweight.paperDevBundle("1.21.3-R0.1-SNAPSHOT")

    compileOnly("org.jetbrains:annotations:24.0.1")

    compileOnly("org.projectlombok:lombok:1.18.30")
    annotationProcessor("org.projectlombok:lombok:1.18.30")
}

tasks.processResources {
    filesMatching("plugin.yml") {
        expand("version" to project.version)
    }
    outputs.upToDateWhen { false }
}