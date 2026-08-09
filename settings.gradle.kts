dependencyResolutionManagement {
    @Suppress("UnstableApiUsage")
    repositories {
        mavenCentral()
        maven {
            name = "PaperMC"
            url = uri("https://repo.papermc.io/repository/maven-public/")
        }
    }
}

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.8.0"
}

rootProject.name = "stick"

include(":stick-core")

include(":stick-paper")
project(":stick-paper").projectDir = file("stick-minecraft/stick-paper")

include(":stick-velocity")
project(":stick-velocity").projectDir = file("stick-minecraft/stick-velocity")
