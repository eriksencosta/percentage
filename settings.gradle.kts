plugins {
    // Apply the foojay-resolver plugin to allow automatic download of JDKs
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.7.0"
}

rootProject.name = "percentage"

include("percentage")
project(":percentage").apply {
    projectDir = file("lib")
}