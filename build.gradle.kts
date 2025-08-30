plugins {
    id("org.jetbrains.dokka") version "2.0.0"
    id("io.gitlab.arturbosch.detekt") version "1.23.8"
}

subprojects {
    apply(plugin = "org.jetbrains.dokka")
//    apply(plugin = "io.gitlab.arturbosch.detekt").also {
//        detekt {
//            buildUponDefaultConfig = true
//            allRules = false
//        }
//
//        tasks.withType<io.gitlab.arturbosch.detekt.Detekt>().configureEach {
//            exclude("**/test/**")
//        }
//    }

}

tasks.dokkaHtmlMultiModule {
    outputDirectory.set(layout.buildDirectory.dir("dokka/html"))
}
