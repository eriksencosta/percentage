import cl.franciscosolis.sonatypecentralupload.SonatypeCentralUploadTask
import java.util.Locale

// TODO: this is reserved for automating snapshot builds.
// This will require changes on sonatype-central-upload.

// Create a snapshot release by default.
val snapshotOption = System.getenv("SNAPSHOT_BUILD").let {
    it.isNullOrBlank() || "yes" == it.lowercase(Locale.getDefault())
}

val snapshot = if (snapshotOption) "-SNAPSHOT" else ""

group = "com.eriksencosta"
version = System.getenv("BUILD_VERSION").let {
    (it?.ifBlank { "0.0.0" } ?: "0.0.0").let { version ->
        "%s%s".format(version, snapshot)
    }
}

plugins {
    // Apply the org.jetbrains.kotlin.jvm Plugin to add support for Kotlin.
    alias(libs.plugins.jvm)
    `java-library`
    `maven-publish`

    id("cl.franciscosolis.sonatype-central-upload") version "1.0.3"
}

repositories {
    mavenCentral()
}

dependencies {
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
    testImplementation(libs.junit.jupiter.engine)
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(8))
    }
    withJavadocJar()
    withSourcesJar()
}

tasks.named<Test>("test") {
    useJUnitPlatform()
}

tasks.jar {
    base {
        manifest {
            attributes(mapOf(
                "Implementation-Title" to "${project.group}.${project.name}",
                "Implementation-Version" to project.version
            ))
        }
    }
}

publishing {
    publications {
        create<MavenPublication>("pom") {
            pom {
                description = "Percentage calculation made easy"
                url = "https://github.com/eriksencosta/percentage"

                developers {
                    developer {
                        name = "Eriksen Costa"
                        url = "https://blog.eriksen.com.br"
                    }
                }

                licenses {
                    license {
                        name = "Apache License, Version 2.0"
                        url = "https://www.apache.org/licenses/LICENSE-2.0.txt"
                    }
                }

                scm {
                    url = "git@github.com:eriksencosta/percentage.git"
                    tag = "trunk"
                }

                issueManagement {
                    system = "GitHub"
                    url = "https://github.com/eriksencosta/percentage/issues"
                }

                ciManagement {
                    system = "GitHub Actions"
                    url = "https://github.com/eriksencosta/percentage/actions"
                }

                // Dependencies
                from(components["java"])
            }
        }
    }
}

tasks.named("build") {
    dependsOn("generatePomFileForPomPublication")
}

tasks.named("sonatypeCentralUpload") {
    mustRunAfter("build")
}

tasks.register("release") {
    description = "Build and publish the library to Maven Central."
    group = "Release"

    dependsOn("build", "sonatypeCentralUpload")
    mustRunAfter("sonatypeCentralUpload")
}

tasks.register("version") {
    description = "Prints the version stamp."
    group = "Verification"

    doLast {
        val versionMessage = "The current version stamp is: $version."
        val snapshotMessage = "The snapshot option is: %s.".format(if (snapshotOption) "on" else "off")

        logger.lifecycle(versionMessage)
        logger.lifecycle(snapshotMessage)
    }
}

tasks.named<SonatypeCentralUploadTask>("sonatypeCentralUpload") {
    username = System.getenv("MAVEN_CENTRAL_USERNAME")
    password = System.getenv("MAVEN_CENTRAL_PASSWORD")

    archives = files(*jars())
    pom = file("build/publications/pom/pom-default.xml")

    signingKey = System.getenv("GPG_PRIVATE_KEY")
    signingKeyPassphrase = System.getenv("GPG_PRIVATE_KEY_PASSWORD")
    publicKey = System.getenv("GPG_PUBLIC_KEY")

    publishingType = "MANUAL"
}

fun jars() = arrayOf(jarName(), jarName("javadoc"), jarName("sources"))

fun jarName(kind: String = "") = "build/libs/$name-%s%s.jar"
    .format(version, if (kind.isNotBlank()) "-$kind" else "")