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
            name = "sonatype"
            url = uri("https://oss.sonatype.org/content/groups/public/")
        }
    }

    dependencies {
        compileOnly("com.destroystokyo.paper:paper-api:1.15.2-R0.1-SNAPSHOT")

        implementation("org.projectlombok:lombok:1.18.12")
    }
}

configure<JavaPluginConvention> {
    sourceCompatibility = JavaVersion.VERSION_1_8
}