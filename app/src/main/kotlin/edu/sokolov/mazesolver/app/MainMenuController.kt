package edu.sokolov.mazesolver.app

import javafx.fxml.FXML
import javafx.fxml.FXMLLoader
import javafx.scene.Parent
import javafx.scene.Scene
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

    }
}