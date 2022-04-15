# Maze solver

## Application on Java-FX & Kotlin for maze solution

Hi there! It is an educational project for 
"Algorithms and data structure" discipline.

### Maze solution ways:
* A* (search finish by distance to finish)
* Breadth first search

### The project consists of two parts:
* `solver` describes Maze configuration and solution ways
* `app` implements `solver` and view it


## How to run it?
* You can run app by running `gradle :app:run` task.
* Or you can build *jar-file*
with command `gradle :app:fatJar` and then start with JDK/JRE
by `java -jar <PATH TO GENERATED FILE>`. Default path to
generated file is **"/app/build/libs/app-1.0-SNAPSHOT.jar"**