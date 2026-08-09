plugins {
    id("buildsrc.convention.kotlin-jvm")
}

dependencies {
    implementation(project(":stick-core"))
    compileOnly(libs.velocityApi)
    testImplementation(libs.velocityApi)
}
