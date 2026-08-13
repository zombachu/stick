package buildsrc.convention

import org.gradle.api.tasks.testing.logging.TestLogEvent
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    `maven-publish`
    kotlin("jvm")
    id("org.jetbrains.dokka")
    id("io.gitlab.arturbosch.detekt")
    id("com.ncorti.ktfmt.gradle")
}

group = "com.zombachu.stick"
version = "0.4.0"

kotlin {
    jvmToolchain(21)
}

java {
    withSourcesJar()
}

publishing {
    publications.create<MavenPublication>("maven") {
        from(components["java"])
    }
}

detekt {
    buildUponDefaultConfig = true
    allRules = false
    source.setFrom(files("src/main/kotlin"))
}

ktfmt {
    kotlinLangStyle()
    maxWidth = 120
    srcSetPathExclusionPattern = Regex(".*test.*")
}

dependencies {
    testImplementation(kotlin("test"))
}

tasks {
    findByName("ktfmtFormatScripts")?.enabled = false
    withType<com.ncorti.ktfmt.gradle.tasks.KtfmtCheckTask>().configureEach {
        enabled = false
    }

    withType<KotlinCompile>().configureEach {
        compilerOptions {
            freeCompilerArgs.set(listOf(
                "-Xcollection-literals",
                "-Xreturn-value-checker=full",
            ))
        }
        dependsOn("ktfmtFormat")
    }

    withType<Test>().configureEach {
        useJUnitPlatform()
        testLogging {
            events(
                TestLogEvent.FAILED,
                TestLogEvent.PASSED,
                TestLogEvent.SKIPPED
            )
        }

        failOnNoDiscoveredTests = false
    }
}
