plugins {
    `kotlin-dsl`
}

kotlin {
    jvmToolchain(25)
}

dependencies {
    implementation(libs.kotlinGradlePlugin)
    implementation(libs.dokkaGradlePlugin)
    implementation(libs.detektGradlePlugin)
    implementation(libs.ktfmtGradlePlugin) {
        exclude(group = "org.jetbrains.kotlin", module = "kotlin-stdlib")
    }
}
