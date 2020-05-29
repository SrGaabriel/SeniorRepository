plugins {
    java
}

group = "com.wizard.gabriel.project"
version = "1.0-SNAPSHOT"

dependencies {
    implementation(":raid-plugin")
}

configure<JavaPluginConvention> {
    sourceCompatibility = JavaVersion.VERSION_1_8
}