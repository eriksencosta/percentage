pluginManagement {
    resolutionStrategy {
        eachPlugin {
            if ("cl.franciscosolis.sonatype-central-upload" == requested.id.toString()) {
                useModule("com.github.eriksencosta:SonatypeCentralUpload:publishing-type-option-jitpack-SNAPSHOT")
            }
        }
    }

    repositories {
        gradlePluginPortal()
        maven("https://jitpack.io")
    }
}

plugins {
    // Apply the foojay-resolver plugin to allow automatic download of JDKs
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.7.0"
}

rootProject.name = "percentage"

include("percentage")
project(":percentage").apply {
    projectDir = file("lib")
}