import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.gradle.api.tasks.bundling.Jar

plugins {
    kotlin("jvm") version "1.6.20"
    id("signing")
    `maven-publish`
}

val _group = "io.github.zbvs"
val _version = "1.0.0"
val _artifact = "nexus-deploy-test"

group = _group
version = _version

repositories {
    mavenCentral()
}

dependencies {
    // implementation("org.apache.logging.log4j:log4j-api-kotlin:1.2.0")
    testImplementation(kotlin("test"))
}


tasks.test {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}

val sourcesJar by tasks.creating(Jar::class) {
    archiveClassifier.set("sources")
    from(sourceSets.main.map { it.allSource })
}

val javadocJar by tasks.creating(Jar::class) {
    val javadoc = tasks.named("javadoc")
    dependsOn(javadoc)
    archiveClassifier.set("javadoc")
    from(javadoc)
}

publishing {
    repositories {


        maven {
            name = "ossRelease"
            url = uri("https://s01.oss.sonatype.org/service/local/staging/deploy/maven2/")
//            url = uri("https://s01.oss.sonatype.org/content/repositories/snapshots")
            val user = project.property("NEXUS_USERNAME").toString()
            val pass = project.property("NEXUS_PASSWROD").toString()

            credentials {
                username = user
                password = pass
            }
        }
    }

    publications {

        create<MavenPublication>(project.name) {
            from(components["kotlin"])

            artifact(sourcesJar)
            artifact(javadocJar)

            groupId = _group
            artifactId = _artifact
            version = _version

            pom {
                packaging = "jar"
                name.set("pom: nexus-deploy-test-name")
                description.set("pom: nexus-deploy-test-desc")
                url.set("http://www.example.com/example-application")
                withXml {
                    asNode().appendNode("packaging", "jar")
                }
                licenses {
                    license {
                        name.set("The Apache Software License, Version 2.0")
                        url.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
                        distribution.set("repo")
                    }
                }
                developers {
                    developer {
                        id.set("zbvs")
                        name.set("zbvs")
                        email.set("zbvs12@gmail.com")
                    }
                }
                scm {
                    url.set("https://github.com/zbvs/nexus-deploy-test")
                    connection.set("https://github.com/zbvs/nexus-deploy-test")
                    developerConnection.set("https://github.com/zbvs/nexus-deploy-test")
                }
            }

            versionMapping {
                usage("java-api") {
                    fromResolutionOf("runtimeClasspath")
                }
                usage("java-runtime") {
                    fromResolutionResult()
                }
            }
        }
    }
}

signing {
    sign(publishing.publications[project.name])
}
