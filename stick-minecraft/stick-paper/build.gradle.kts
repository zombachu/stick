plugins {
    id("buildsrc.convention.kotlin-jvm")
    alias(libs.plugins.paperweightUserdev)
}

dependencies {
    implementation(project(":stick-core"))
    paperweight.paperDevBundle(libs.versions.paperDevBundle.get())
}
