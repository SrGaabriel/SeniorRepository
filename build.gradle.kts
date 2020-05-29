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
        maven {
            url = uri("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
            content {
                includeGroup("org.bukkit")
                includeGroup("org.spigotmc")
            }
        }
        maven {
            name = "sonatype"
            url = uri("https://oss.sonatype.org/content/groups/public/")
        }
        maven("https://repo.extendedclip.com/content/repositories/placeholderapi/")
    }

    dependencies {
        compileOnly("com.destroystokyo.paper:paper-api:1.12.2-R0.1-SNAPSHOT")

        compileOnly(fileTree("dependencies"))
        compileOnly("me.clip:placeholderapi:2.10.6")
    }
}

configure<JavaPluginConvention> {
    sourceCompatibility = JavaVersion.VERSION_1_8
}