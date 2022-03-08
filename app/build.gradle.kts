plugins {
    kotlin("jvm")
    application
    id("org.openjfx.javafxplugin") version "0.0.10"
    id("org.beryx.jlink") version "2.24.1"
}

group = "edu.sokolov"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

application {
    mainClass.set("edu.sokolov.mazesolver.app.HelloApplication")
}

javafx {
    version = "14.0.2.1"
    modules("javafx.controls", "javafx.fxml")
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation(project(":solver"))
    testImplementation(kotlin("test"))

    implementation("org.controlsfx:controlsfx:11.1.1")
    implementation("com.dlsc.formsfx:formsfx-core:11.4.2") {
        exclude(group = "org.openjfx")
    }
    implementation("net.synedra:validatorfx:0.2.1") {
        exclude(group = "org.openjfx")
    }
}

tasks.test {
    useJUnitPlatform()
}