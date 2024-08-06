plugins {
    id("java")
    id("idea")
    id("io.papermc.paperweight.userdev") version("1.5.11")
}

group = "net.njsharpe"
version = "20240806-1.10.0-SNAPSHOT"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(17)
    }
    withJavadocJar()
    withSourcesJar()
}

repositories {
    mavenCentral()
}

dependencies {
    paperweight.paperDevBundle("1.20.4-R0.1-SNAPSHOT")

    compileOnly("org.jetbrains:annotations:24.0.1")

    compileOnly("org.projectlombok:lombok:1.18.28")
    annotationProcessor("org.projectlombok:lombok:1.18.28")
}

tasks.processResources {
    filesMatching("plugin.yml") {
        expand("version" to project.version)
    }
    outputs.upToDateWhen { false }
}