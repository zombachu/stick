plugins {
    // Apply the shared build logic from a convention plugin.
    // The shared code is located in `buildSrc/src/main/kotlin/kotlin-jvm.gradle.kts`.
    id("buildsrc.convention.kotlin-jvm")
}

repositories {
    maven {
        name = "Paper"
        url = uri("https://repo.papermc.io/repository/maven-public/")
    }
}

dependencies {
    implementation(project(":stick-core"))

    compileOnly("com.velocitypowered:velocity-api:3.4.0-SNAPSHOT")

    testImplementation(kotlin("test"))
}
