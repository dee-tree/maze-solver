package edu.sokolov.mazesolver.app

import java.io.File
import javax.swing.filechooser.FileSystemView

val mazeAppDir = File(FileSystemView.getFileSystemView().defaultDirectory, "maze-solver").apply { mkdir() }
val currentMazeConfigFile = File(mazeAppDir, "maze.mz")