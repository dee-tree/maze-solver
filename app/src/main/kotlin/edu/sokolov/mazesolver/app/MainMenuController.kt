package edu.sokolov.mazesolver.app

import javafx.fxml.FXML
import javafx.fxml.FXMLLoader
import javafx.scene.Parent
import javafx.scene.Scene
import javafx.scene.control.Alert
import javafx.scene.control.Alert.AlertType
import javafx.stage.Stage

class MainMenuController {

    @FXML
    private fun onConfigureMazeButtonClick() {

        val fxmlLoader = FXMLLoader(MainMenuController::class.java.getResource("maze-configuration-view.fxml"))
        val root = fxmlLoader.load<Parent>()
        val stage = Stage()
        stage.scene = Scene(root)

        stage.show()
    }


    @FXML
    private fun onSolveMazeButtonClick() {
        // Если не выбран файл с конфигурацией лабиринта, то выдаем пользователю ошибку
        if (!currentMazeConfigFile.isFile) {
            Alert(AlertType.ERROR).apply {
                title = "Oops!"
                contentText = "You must set config file before this action!"
                showAndWait()
            }

            return
        }

        // Если не выбран способ решения лабиринта, выдаем пользователю ошибку
        if (mazeMetrics == null) {
            Alert(AlertType.ERROR).apply {
                title = "Oops!"
                contentText = "You must set solution way before this action!"
                showAndWait()
            }

            return
        }

        // Запуск окна с решателем лабиринта
        val fxmlLoader = FXMLLoader(MainMenuController::class.java.getResource("maze-solver-view.fxml"))
        val root = fxmlLoader.load<Parent>()
        val stage = Stage()
        stage.scene = Scene(root)

        stage.show()
    }
}