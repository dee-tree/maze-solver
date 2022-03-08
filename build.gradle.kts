plugins {
    kotlin("jvm") version "1.6.10"
//    application
//    id("org.openjfx.javafxplugin") version "0.0.10"
//    id("org.beryx.jlink") version "2.24.1"
}

group = "edu.sokolov"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

//application {
//    mainClass.set("edu.sokolov.mazesolver.HelloApplication")
//}
//
//javafx {
//    version = "14.0.2.1"
//    modules("javafx.controls", "javafx.fxml")
//}

//dependencies {
//    implementation(kotlin("stdlib"))
//    testImplementation(kotlin("test"))
//
//    implementation("org.controlsfx:controlsfx:11.1.1")
//    implementation("com.dlsc.formsfx:formsfx-core:11.4.2") {
//        exclude(group = "org.openjfx")
//    }
//    implementation("net.synedra:validatorfx:0.2.1") {
//        exclude(group = "org.openjfx")
//    }
//}

//tasks.test {
//    useJUnitPlatform()
//}

/*

plugins {
    id 'java'
    id 'application'
    id 'org.jetbrains.kotlin.jvm' version '1.5.31'
    id 'org.openjfx.javafxplugin' version '0.0.10'
    id 'org.beryx.jlink' version '2.24.1'
}

group 'edu.sokolov'
version '1.0-SNAPSHOT'

repositories {
    mavenCentral()
}


sourceCompatibility = '14'
targetCompatibility = '14'

tasks.withType(JavaCompile) {
    options.encoding = 'UTF-8'
}

application {
    mainModule = 'edu.sokolov.labsolver'
    mainClass = 'edu.sokolov.labsolver.HelloApplication'
}

[compileKotlin, compileTestKotlin].forEach {
    it.kotlinOptions {
        jvmTarget = '11'
    }
}

javafx {
    version = '14.0.2.1'
    modules = ['javafx.controls', 'javafx.fxml']
}

dependencies {
    implementation('org.controlsfx:controlsfx:11.1.0')
    implementation('com.dlsc.formsfx:formsfx-core:11.3.2') {
        exclude(group: 'org.openjfx')
    }
    implementation('net.synedra:validatorfx:0.1.13') {
        exclude(group: 'org.openjfx')
    }
    implementation('org.kordamp.bootstrapfx:bootstrapfx-core:0.4.0')

    testImplementation("org.junit.jupiter:junit-jupiter-api:${junitVersion}")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:${junitVersion}")
}


jlink {
    imageZip = project.file("${buildDir}/distributions/app-${javafx.platform.classifier}.zip")
    options = ['--strip-debug', '--compress', '2', '--no-header-files', '--no-man-pages']
    launcher {
        name = 'app'
    }
}

jlinkZip {
    group = 'distribution'
}

* */