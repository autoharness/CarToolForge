import org.autoharness.forge.VehiclePropertyConfigGenerator
import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.ksp)
    alias(libs.plugins.android.git.version)
}
apply(from = "../deliver.gradle")

androidGitVersion {
    codeFormat = "MMNNNPPP"
    hideBranches = listOf()
    format = "%tag%%-branch%%-commit%%-dirty%"
}

android {
    namespace = "org.autoharness.cartool"
    compileSdk {
        version = release(37)
    }

    defaultConfig {
        applicationId = "org.autoharness.cartool"
        minSdk = 36
        targetSdk = 37
        versionCode = if (androidGitVersion.code() == 0) 1 else androidGitVersion.code()
        versionName = androidGitVersion.name()

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        useLibrary("android.car")
    }

    val releaseKeystoreFile = rootDir.resolve("keystore/release.keystore")
    val releasePropFile = rootDir.resolve("keystore/release.properties")
    val withReleaseSigning = releaseKeystoreFile.exists() && releasePropFile.exists()

    signingConfigs {
        getByName("debug") {
            storeFile = rootDir.resolve("keystore/debug.keystore")
            storePassword = "debugKey"
            keyAlias = "debugKey"
            keyPassword = "debugKey"
            enableV1Signing = true
            enableV2Signing = true
        }

        if (withReleaseSigning) {
            val releaseProps = Properties()
            releasePropFile.inputStream().use { releaseProps.load(it) }

            create("release_signing") {
                storeFile = releaseKeystoreFile
                storePassword = releaseProps.getProperty("storePassword")
                keyAlias = releaseProps.getProperty("keyAlias")
                keyPassword = releaseProps.getProperty("keyPassword")
                enableV1Signing = true
                enableV2Signing = true
            }
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro",
            )
            if (withReleaseSigning) {
                signingConfig = signingConfigs.getByName("release_signing")
            }
        }
        debug {
            versionNameSuffix = "-debug"
            isMinifyEnabled = false
            isShrinkResources = false
            isDebuggable = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro",
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}

ksp {
    arg("appfunctions:aggregateAppFunctions", "true")
    arg("appfunctions:generateMetadataFromSchema", "true")
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.appfunctions)
    implementation(libs.appfunctions.service)
    ksp(libs.appfunctions.compiler)
    implementation(libs.guava.android)
    implementation(project(":CarLibSystemPackage"))
    testImplementation(libs.junit)
    testImplementation(libs.mockito.core)
    testImplementation(libs.mockito.kotlin)
    testImplementation(libs.robolectric)
}

/** Vehicle property config generation */
abstract class GenerateCodeTask : DefaultTask() {
    @get:InputFile
    abstract val configInput: RegularFileProperty

    @get:OutputDirectory
    abstract val outputDir: DirectoryProperty

    @TaskAction
    fun generate() {
        VehiclePropertyConfigGenerator.generate(
            input = configInput.get().asFile,
            outDir = outputDir.get().asFile,
        )
    }
}

val generateCodeFromConfig =
    tasks.register<GenerateCodeTask>("generateCodeFromConfig") {
        group = "build"
        description = "Generates kotlin code from the config file."
        configInput.set(rootProject.file("config/vehicle_properties.yaml"))
        outputDir.set(layout.buildDirectory.dir("generated/source/kotlin/main"))
    }

androidComponents {
    onVariants { variant ->
        variant.sources.java?.addGeneratedSourceDirectory(
            generateCodeFromConfig,
            GenerateCodeTask::outputDir,
        )
    }
}
