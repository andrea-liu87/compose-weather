plugins {
    // this is necessary to avoid the plugins to be loaded multiple times
    // in each subproject's classloader
    alias(libs.plugins.androidApplication) apply false
    alias(libs.plugins.androidLibrary) apply false
    alias(libs.plugins.jetbrainsCompose) apply false
    alias(libs.plugins.compose.compiler) apply false
    alias(libs.plugins.kotlinMultiplatform) apply false
    alias(libs.plugins.kotlinAndroid).apply(false)
    alias(libs.plugins.kotlinCocoapods).apply(false)
    kotlin("jvm").version("1.9.22").apply(false)
    kotlin("plugin.serialization").version("1.9.22").apply(false)
    id("com.google.dagger.hilt.android") version "2.48" apply false
}