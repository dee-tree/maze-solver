package edu.sokolov.mazesolver.app

import javafx.application.Application
import javafx.fxml.FXMLLoader
import javafx.scene.Scene
import javafx.stage.Stage

/**
 * Класс, описывающий GUI приложение решателя лабиринта
 */
class MazeSolverApplication : Application() {
    override fun start(stage: Stage) {
        val fxmlLoader = FXMLLoader(MazeSolverApplication::class.java.getResource("mainmenu-view.fxml"))
        val scene = Scene(fxmlLoader.load(), 320.0, 240.0)
        stage.title = "MazeSo!ver"
        stage.scene = scene
        stage.show()
    }
}

/**
 * Начальная точка запуска приложения
 */
fun main() {
    Application.launch(MazeSolverApplication::class.java)
}