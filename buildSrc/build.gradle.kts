import org.gradle.kotlin.dsl.`kotlin-dsl`

plugins {
    `kotlin-dsl`
    alias(libs.plugins.spotless) apply false
}
apply(plugin = "com.diffplug.spotless")
apply(from = "../spotless.gradle")

dependencies {
    implementation(libs.yaml.snakeyaml)
    implementation(libs.squareup.kotlinpoet)

    testImplementation(libs.junit)
}
