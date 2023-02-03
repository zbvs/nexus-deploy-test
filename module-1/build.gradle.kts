plugins {
    kotlin("jvm")
    id("signing")
    `maven-publish`
}

group = _group
version = _version

repositories {
    mavenCentral()
}

dependencies {
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.1")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.8.1")
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}

fun setPublishingContext(){
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
        publications {

            create<MavenPublication>(project.name) {
                from(components["kotlin"])

                artifact(sourcesJar)
                artifact(javadocJar)

                groupId = _group
                artifactId = "module-1"
                version = _version

                pom {
                    packaging = "jar"
                    name.set("pom: nexus-deploy-test-name")
                    description.set("pom: nexus-deploy-test-desc")
                    url.set("http://www.example.com/example-application")

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
        val secretKey = AES256Decrypt(secretKey, aesKeyPhrase.get())
        val password = AES256Decrypt(seckey_password, aesKeyPhrase.get())
        useInMemoryPgpKeys(secretKey, password)
        sign(publishing.publications[project.name])
    }
}

if (project.gradle.startParameter.taskNames.contains("publish")) {
    setPublishingContext()
}
