import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.gradle.api.tasks.bundling.Jar

plugins {
    kotlin("jvm") version "1.6.20"
    id("signing")
    `maven-publish`
    id("io.github.gradle-nexus.publish-plugin") version "1.1.0"
}



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

group = _group
version = _version

println("version : ${project.version}")
repositories {
    mavenCentral()
}


fun setPublishingContext() {
    version = _version
    // https://groups.google.com/a/glists.sonatype.com/g/ossrh-users/c/pZvR9L3HvvY?pli=1
    // https://solidsoft.wordpress.com/2019/02/22/reliable-releasing-to-maven-central-from-travis-using-gradle-2019-edition/
    // https://github.com/Codearte/gradle-nexus-staging-plugin
    // https://github.com/gradle-nexus/publish-plugin/

    nexusPublishing {
        repositories {
            sonatype {
                nexusUrl.set(uri("https://s01.oss.sonatype.org/service/local/"))
                snapshotRepositoryUrl.set(uri("https://s01.oss.sonatype.org/content/repositories/snapshots"))
                username.set(AES256Decrypt(nexus_username, aesKeyPhrase.get()))
                password.set(AES256Decrypt(nexus_password, aesKeyPhrase.get()))
            }
        }
    }
}



if (project.gradle.startParameter.taskNames.contains("publish")) {
    if ( !project.hasProperty(AES_KEY_ARGUMENT) ) {
        throw GradleException("./gradlew {taskname} -P${AES_KEY_ARGUMENT}={your-aes-key}")
    }

    aesKeyPhrase.set(project.property(AES_KEY_ARGUMENT).toString())
    setPublishingContext()
}


