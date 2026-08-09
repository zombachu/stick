plugins {
    `kotlin-dsl`
}

kotlin {
    jvmToolchain(21)
}

dependencies {
    implementation(libs.kotlinGradlePlugin)
    implementation(libs.dokkaGradlePlugin)
    implementation(libs.detektGradlePlugin)
    implementation(libs.ktfmtGradlePlugin) {
        exclude(group = "org.jetbrains.kotlin", module = "kotlin-stdlib")
    }
}
