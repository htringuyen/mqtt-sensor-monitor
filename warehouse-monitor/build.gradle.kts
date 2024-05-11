plugins {
    id("java")
    id("org.openjfx.javafxplugin") version "0.1.0"
    alias(libs.plugins.lombok)
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}



javafx {
    version = "21"
    modules("javafx.controls", "javafx.fxml")
}

dependencies {
    implementation(libs.hivemq.mqttClient)
    implementation(libs.jackson.core)
    implementation(libs.jackson.databind)
    implementation(libs.logback.classic)
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.test {
    useJUnitPlatform()
}