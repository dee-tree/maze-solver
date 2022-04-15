package edu.sokolov.mazesolver.app

import edu.sokolov.maze.Metrics
import java.io.File
import javax.swing.filechooser.FileSystemView

/**
 * Директория для хранения конфигурационного файла лабиринта
 */
val mazeAppDir = File(FileSystemView.getFileSystemView().defaultDirectory, "maze-solver").apply { mkdir() }

/**
 * Файл с текущей конфигурацией лабиринта
 */
val currentMazeConfigFile = File(mazeAppDir, "maze.mz")

/**
 * Выбранная метрика для решателя.
 * Описывает способ решения лабиринта: поиск в ширину или A* (поиск по близости к финишу)
 */
var mazeMetrics: Metrics? = null