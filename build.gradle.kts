plugins {
    java
}

group = "com.wizard.gabriel.project"
version = "1.0-SNAPSHOT"

allprojects {
    subprojects{ apply(plugin = "java") }

    repositories {
        mavenCentral()
        maven {
            name = "papermc-repo"
            url = uri("https://papermc.io/repo/repository/maven-public/")
        }
        maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
        maven("https://maven.enginehub.org/repo/")
        maven("https://mvn.intellectualsites.com/content/groups/public/")
        maven {
            name = "sonatype"
            url = uri("https://oss.sonatype.org/content/groups/public/")
        }
        maven("https://repo.extendedclip.com/content/repositories/placeholderapi/")
    }

    dependencies {
        compileOnly("com.destroystokyo.paper:paper-api:1.12.2-R0.1-SNAPSHOT")

        compileOnly(fileTree("dependencies"))
    }
}

configure<JavaPluginConvention> {
    sourceCompatibility = JavaVersion.VERSION_1_8
}