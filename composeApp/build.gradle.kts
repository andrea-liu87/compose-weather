
import org.jetbrains.compose.ExperimentalComposeLibrary
import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import com.codingfeline.buildkonfig.compiler.FieldSpec.Type.STRING
import org.jetbrains.kotlin.konan.properties.Properties

buildscript {
    repositories {
        mavenCentral()
    }
    dependencies {
        classpath(libs.buildkonfig.gradle.plugin)
        classpath(libs.kotlin.gradle.plugin)
        classpath(libs.agp)
        classpath(libs.compose.multiplatform)
        classpath(libs.kotlin.serialization)
        classpath(libs.molecule.gradle.plugin)
    }
}

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsCompose)
    alias(libs.plugins.compose.compiler)
    id("com.codingfeline.buildkonfig") version "+"
    kotlin("plugin.serialization")
    id("kotlin-parcelize")
    alias(libs.plugins.kotlinCocoapods)
}

kotlin {
    androidTarget {
        compilations.all {
            kotlinOptions {
                jvmTarget = "17"
            }
        }
    }

    task("testClasses")
    
    jvm("desktop")
    
    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "composeApp"
            isStatic = true
            export(libs.decompose.router)
        }
    }

    cocoapods {
        version = "1.0.0"
        summary = "Some description for the Shared Module"
        homepage = "Link to the Shared Module homepage"
        ios.deploymentTarget = "16.0"
        podfile = project.file("../iosApp/Podfile")
        framework {
            baseName = "composeApp"
            isStatic = true
        }
    }
    
    sourceSets {
        val desktopMain by getting {
            dependencies {
                implementation(libs.ktor.client.cio)
                implementation(libs.kstore.file)
                implementation(compose.desktop.currentOs)
                implementation(libs.harawata.appdirs)
                implementation(libs.compose.ui.tooling.preview.jetbrain)
            }
        }
        
        val androidMain by getting {
            dependencies {
                implementation(libs.compose.ui.tooling.preview)
                implementation(libs.androidx.activity.compose)
                implementation(libs.ktor.client.cio)
                implementation(libs.kstore.file)
                implementation ("com.google.accompanist:accompanist-systemuicontroller:0.27.0")
                implementation("androidx.startup:startup-runtime:1.1.1")
            }
        }

        val commonMain by getting {
            dependencies {
                api(libs.decompose.router)
                implementation(compose.runtime)
                implementation(compose.foundation)
                implementation(compose.material3)
                implementation(compose.material)
                implementation(compose.ui)
                @OptIn(ExperimentalComposeLibrary::class)
                implementation(compose.components.resources)
                implementation("org.jetbrains.kotlin:kotlin-stdlib-common")
                implementation(libs.kotlinx.coroutines)
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.5.1")

                implementation(libs.molecule.runtime)
                implementation(libs.decompose)
                implementation(libs.decompose.compose.multiplatform)
                implementation(libs.decompose.router)

                implementation(libs.essenty.parcelable)
                implementation(libs.ktor.client.core)
                implementation(libs.ktor.client.content.negotiation)
                implementation(libs.ktor.client.logging)
                implementation(libs.ktor.serialization.kotlinx.json)
                implementation(libs.kstore)

                implementation(libs.google.play.services.android.location)

                api("org.lighthousegames:logging:1.3.0")

                implementation(libs.kotlinx.datetime)

                api("io.github.qdsfdhvh:image-loader:1.7.8")
            }
        }

        val iosX64Main by getting
        val iosArm64Main by getting
        val iosSimulatorArm64Main by getting
        val iosMain by creating {
            dependsOn(commonMain)
            iosX64Main.dependsOn(this)
            iosArm64Main.dependsOn(this)
            iosSimulatorArm64Main.dependsOn(this)
            dependencies {
                implementation(libs.ktor.client.darwin)
                implementation(libs.kstore.file)
            }
        }
    }
}

android {
    namespace = "com.andreasgift.composeweather"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    sourceSets["main"].res.srcDirs("src/androidMain/res")
    sourceSets["main"].resources.srcDirs("src/commonMain/resources")

    defaultConfig {
        applicationId = "com.andreasgift.composeweather"
        minSdk = libs.versions.android.minSdk.get().toInt()
        targetSdk = libs.versions.android.targetSdk.get().toInt()
        versionCode = 1
        versionName = "1.0"
    }
//    packaging {
//        resources {
//            excludes += "/META-INF/{AL2.0,LGPL2.1}"
//        }
//    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}

compose.desktop {
    application {
        mainClass = "MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "com.andreasgift.composeweather"
            packageVersion = "1.0.0"
        }
    }
}

buildkonfig {
    val propertiesProj = Properties()
    packageName = "com.andreasgift.kmpweatherapp"

    defaultConfigs {
        propertiesProj.load(project.rootProject.file("local.properties").inputStream())
        val apiKey: String = propertiesProj.getProperty("API_KEY")
        val GMapsKey: String = propertiesProj.getProperty("GOOGLE_MAPS_KEY")

        require(apiKey.isNotEmpty()) {
            "Register your api key from open weather and place it in local.properties as `API_KEY`"
        }

        buildConfigField(STRING, "API_KEY", apiKey)
        buildConfigField(STRING, "GOOGLE_MAPS_KEY", GMapsKey)
    }
}
