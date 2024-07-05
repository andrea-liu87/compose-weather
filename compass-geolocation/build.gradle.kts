//import dev.jordond.compass.convention.configureMultiplatform

plugins {
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.poko)
    alias(libs.plugins.dokka)
    alias(libs.plugins.publish)
    //alias(libs.plugins.convention.multiplatform)
}

//configureMultiplatform()

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

    sourceSets {
        commonMain.dependencies {
            api(projects.compassCore)
            // TODO ADd these
            api(projects.compassPermissions)

            implementation(libs.kotlinx.coroutines)
        }
    }
}