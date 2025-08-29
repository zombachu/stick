import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

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

val compileKotlin: KotlinCompile by tasks
compileKotlin.compilerOptions {
    freeCompilerArgs.set(listOf("-Xcontext-parameters"))
}
val compileTestKotlin: KotlinCompile by tasks
compileTestKotlin.compilerOptions {
    freeCompilerArgs.set(listOf("-Xcontext-parameters"))
}
