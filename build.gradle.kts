plugins {
    id("org.jetbrains.dokka") version "2.0.0"
    id("io.gitlab.arturbosch.detekt") version "1.23.8"
}

subprojects {
    apply(plugin = "org.jetbrains.dokka")
    apply(plugin = "io.gitlab.arturbosch.detekt").also {
        detekt {
            buildUponDefaultConfig = true
            allRules = false
            source.setFrom(files("src/main/kotlin"))
        }
    }
}

tasks.dokkaHtmlMultiModule {
    outputDirectory.set(layout.buildDirectory.dir("dokka/html"))
}
