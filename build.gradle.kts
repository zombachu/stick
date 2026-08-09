plugins {
    id("org.jetbrains.dokka")
}

tasks.dokkaHtmlMultiModule {
    outputDirectory.set(layout.buildDirectory.dir("dokka/html"))
}
