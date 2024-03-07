import cl.franciscosolis.sonatypecentralupload.SonatypeCentralUploadTask
import java.util.Locale

// TODO: this is reserved for automating snapshot builds. Create a snapshot release by default.
val snapshotOption = System.getenv("SNAPSHOT_BUILD").let {
    it.isNullOrBlank() || "yes" == it.lowercase(Locale.getDefault())
}

val snapshot = if (snapshotOption) "-snapshot" else ""

group = "com.eriksencosta"
version = "%s%s".format(version, snapshot)

plugins {
    `java-library`
    `maven-publish`
    jacoco
    alias(libs.plugins.jvm)
    alias(libs.plugins.detekt)
    alias(libs.plugins.dokka)
    alias(libs.plugins.sonatype.central.upload)
}

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(libs.kotlin.test.junit5)
    testImplementation(libs.junit.jupiter.engine)
    testRuntimeOnly(libs.junit.platform.launcher)

    detektPlugins(libs.detekt.formatting)
    detektPlugins(libs.detekt.libraries)
}

java {
    toolchain.languageVersion = JavaLanguageVersion.of(8)
    withSourcesJar()
}

detekt {
    config.setFrom("../detekt.yml")
    buildUponDefaultConfig = true
}

tasks {
    named<Test>("test") {
        useJUnitPlatform()
        finalizedBy("jacocoTestReport")
        dependsOn("detekt")
    }

    named<JacocoReport>("jacocoTestReport") {
        reports.xml.required = true
        sourceSets(sourceSets.main.get())
        dependsOn("test")
    }

    named<Jar>("jar") {
        base {
            manifest {
                attributes(mapOf(
                    "Implementation-Title" to "${project.group}.${project.name}",
                    "Implementation-Version" to project.version
                ))
            }
        }
    }

    named<SonatypeCentralUploadTask>("sonatypeCentralUpload") {
        username = System.getenv("MAVEN_CENTRAL_USERNAME")
        password = System.getenv("MAVEN_CENTRAL_PASSWORD")

        archives = files(*jars())
        pom = file("build/publications/pom/pom-default.xml")

        signingKey = System.getenv("GPG_PRIVATE_KEY")
        signingKeyPassphrase = System.getenv("GPG_PRIVATE_KEY_PASSWORD")
        publicKey = System.getenv("GPG_PUBLIC_KEY")

        publishingType = "MANUAL"

        dependsOn("build")
        mustRunAfter("build")

        // Allows to run the plugin without rebuilding the project due to available jar files in
        // its publishing directory.
        doFirst { delete("build/sonatype-central-upload") }
    }

    named<Task>("build") {
        dependsOn("test", "generatePomFileForPomPublication", "generateJavadocJar")
    }

    register<Jar>("generateJavadocJar") {
        dependsOn(dokkaJavadoc)
        from(dokkaJavadoc.flatMap { it.outputDirectory })
        archiveClassifier.set("javadoc")

        mustRunAfter("sourcesJar")
    }

    register<Task>("generateDocs") {
        // TODO: description and group
        dependsOn("dokkaHtml", "dokkaGfm", "dokkaJavadoc")
    }

    register<Task>("release") {
        description = "Build and publish the library to Maven Central."
        group = "Release"

        dependsOn("build", "sonatypeCentralUpload", "version")
        mustRunAfter("sonatypeCentralUpload", "version")
    }

    register<Task>("version") {
        description = "Prints the version stamp."
        group = "Verification"

        doLast {
            val versionMessage = "The current version stamp is: $version."
            val snapshotMessage = "The snapshot option is: %s.".format(if (snapshotOption) "on" else "off")

            logger.lifecycle(versionMessage)
            logger.lifecycle(snapshotMessage)
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

fun jars() = arrayOf(jarName(), jarName("javadoc"), jarName("sources"))

fun jarName(kind: String = "") = "build/libs/$name-%s%s.jar"
    .format(version, if (kind.isNotBlank()) "-$kind" else "")