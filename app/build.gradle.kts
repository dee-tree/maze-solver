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

val mainClassAppName = "edu.sokolov.mazesolver.app.MazeSolverApplicationKt"

application {
    mainClass.set(mainClassAppName)
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

tasks.create<Jar>("fatJar") {
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE

    manifest {
        attributes["Manifest-Version"] = "1.0"
        attributes["Main-Class"] = mainClassAppName
    }

    from(configurations.compileClasspath.get().map { if (it.isDirectory) it else zipTree(it) })
    with(tasks.jar.get() as CopySpec)
}

tasks.getByName<JavaExec>("run") {
    dependsOn("fatJar")
}

tasks.test {
    useJUnitPlatform()
}