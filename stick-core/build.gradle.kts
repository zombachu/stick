plugins {
    id("buildsrc.convention.kotlin-jvm")
    `java-test-fixtures`
}

dependencies {
    testFixturesImplementation(kotlin("test"))
}

// Test fixtures exist for this project's tests and the adapter modules; they are not part of the
// published library, so keep their variants out of the Maven publication.
val javaComponent = components["java"] as AdhocComponentWithVariants
javaComponent.withVariantsFromConfiguration(configurations["testFixturesApiElements"]) { skip() }
javaComponent.withVariantsFromConfiguration(configurations["testFixturesRuntimeElements"]) { skip() }
