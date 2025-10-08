import org.autoharness.forge.VehiclePropertyConfigGenerator

tasks.register("generateCodeFromConfig") {
    group = "build"
    description = "Generates kotlin code from the config file."

    val configInput = rootProject.file("config/vehicle_properties.yaml")
    val outputDir = layout.buildDirectory.dir("generated/source/kotlin/main")

    inputs.file(configInput)
    outputs.dir(outputDir)

    doLast {
        VehiclePropertyConfigGenerator.generate(
            input = configInput,
            outDir = outputDir.get().asFile,
        )
    }
}

tasks.named("preBuild").get().dependsOn("generateCodeFromConfig")
