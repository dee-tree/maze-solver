package edu.sokolov.mazesolver.app

import edu.sokolov.maze.InvalidMazeFileFormatException
import edu.sokolov.maze.Maze
import edu.sokolov.mazesolver.app.view.toView
import javafx.fxml.FXML
import javafx.scene.control.Alert
import javafx.scene.control.Label
import javafx.scene.layout.GridPane
import javafx.stage.FileChooser
import javafx.stage.Stage

class MazeConfigurationController {

    companion object {
        const val defaultRows = 5
        const val defaultColumns = 5

        const val maxRows = 100
        const val maxColumns = 100

        const val minRows = 2
        const val minColumns = 2
    }

    @FXML
    private lateinit var mazeRows: Label

    @FXML
    private lateinit var mazeColumns: Label

    @FXML
    private lateinit var mazeGrid: GridPane

    private var rows = defaultRows
        set(value) {
            mazeRows.text = value.toString()
            field = value
        }


    private var columns = defaultColumns
        set(value) {
            mazeColumns.text = value.toString()
            field = value
        }

    @FXML
    private fun onIncreaseMazeRowsButtonClick() {
        if (rows < maxRows)
            rows++
    }

    @FXML
    private fun onDecreaseMazeRowsButtonClick() {
        if (rows > minRows)
            rows--
    }

    @FXML
    private fun onIncreaseMazeColumnsButtonClick() {
        if (columns < maxColumns)
            columns++
    }

    @FXML
    private fun onDecreaseMazeColumnsButtonClick() {
        if (columns > minColumns)
            columns--
    }


    @FXML
    private fun onLoadConfigurationFromFileButtonClick() {
        val chooser = FileChooser().apply {
            title = "Choose text file with maze configuration"
            extensionFilters.add(FileChooser.ExtensionFilter("Maze configuration", "*.mz"))
        }

        val selected = chooser.showOpenDialog(mazeGrid.scene.window)

        selected?.let {
            val maze: Maze

            try {
                maze = Maze.fromFile(it.absolutePath)
            } catch (e: InvalidMazeFileFormatException) {
                Alert(Alert.AlertType.ERROR).apply {
                    title = "Error loading maze from file"
                    contentText = "Configuration in the file ($it) is invalid! ${e}"
                    showAndWait()
                    return
                }
            }

            currentMazeConfigFile.writeText(it.readText())


            maze.traverseAll { cell ->
                mazeGrid.add(cell.toView(), cell.column, cell.row)

            }
        }
    }

    @FXML
    private fun onSaveConfigButtonClick() {
        (mazeGrid.scene.window as Stage).close()
    }

    @FXML
    fun initialize() {
        mazeColumns.text = columns.toString()
        mazeRows.text = rows.toString()
    }
}