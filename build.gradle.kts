plugins {
    id("org.jetbrains.dokka") version "2.0.0"
    id("io.gitlab.arturbosch.detekt") version "1.23.8"
    id("com.ncorti.ktfmt.gradle") version "0.26.0"
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
    apply(plugin = "com.ncorti.ktfmt.gradle").also {
        ktfmt {
            kotlinLangStyle()
            maxWidth = 120
            srcSetPathExclusionPattern = Regex(".*test.*")
        }
    }
    tasks.findByName("ktfmtFormatScripts")?.enabled = false
    tasks.withType<com.ncorti.ktfmt.gradle.tasks.KtfmtCheckTask> {
        enabled = false
    }
}

tasks.dokkaHtmlMultiModule {
    outputDirectory.set(layout.buildDirectory.dir("dokka/html"))
}
tasks.findByName("ktfmtFormatScripts")?.enabled = false
tasks.withType<com.ncorti.ktfmt.gradle.tasks.KtfmtCheckTask> {
    enabled = false
}
