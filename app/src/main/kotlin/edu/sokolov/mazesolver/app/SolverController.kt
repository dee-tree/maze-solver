package edu.sokolov.mazesolver.app

import edu.sokolov.maze.Maze
import edu.sokolov.maze.MazeSolver
import edu.sokolov.maze.NoFinishCellException
import edu.sokolov.mazesolver.app.view.CellPane
import edu.sokolov.mazesolver.app.view.toView
import javafx.application.Platform
import javafx.fxml.FXML
import javafx.scene.control.Alert
import javafx.scene.control.Button
import javafx.scene.control.Label
import javafx.scene.control.ToggleGroup
import javafx.scene.layout.GridPane
import java.util.*
import kotlin.concurrent.timerTask

class SolverController {

    companion object {
        const val defaultSpeed = 10
        const val maxSpeed = 20
        const val minSpeed = 1
        const val speedStep = 1
    }

    @FXML
    private lateinit var mazeGrid: GridPane

    @FXML
    private lateinit var stepSpeed: Label

    @FXML
    private lateinit var start: Button

    @FXML
    private lateinit var increaseSpeed: Button

    @FXML
    private lateinit var decreaseSpeed: Button

    @FXML
    private lateinit var pause: Button

    private var currentSpeed: Int = defaultSpeed
        set(value) {
            if (this::timer.isInitialized) {
                onPauseButtonClick()
                onStartButtonClick()
            }
            stepSpeed.text = value.toString()
            field = value
        }


    @FXML
    private fun onIncreaseSpeedButtonClick() {
        if (currentSpeed < maxSpeed)
            currentSpeed += speedStep
    }

    @FXML
    private fun onDecreaseSpeedButtonClick() {
        if (currentSpeed > minSpeed)
            currentSpeed -= speedStep
    }

    /**
     * Вызывается при нажатии на кнопку "старт"
     */
    @FXML
    private fun onStartButtonClick() {
        // Запускается таймер, который для выбранной скорости решения открывает новые шаги решения
        start.isDisable = true
        timer = Timer("maze-solver").apply {
            schedule(
                timerTask { nextStep() },
                ((maxSpeed - currentSpeed + 1) * 10).toLong(),
                ((maxSpeed - currentSpeed + 1) * 10).toLong()
            )
        }

        pause.isDisable = false
    }

    /**
     * Вызывается при нажатии на кнопку "пауза". Останавливает таймер
     */
    @FXML
    private fun onPauseButtonClick() {
        pause.isDisable = true
        timer.cancel()

        start.isDisable = false
    }


    private lateinit var maze: Maze
    private lateinit var mazeSolver: MazeSolver


    private lateinit var timer: Timer


    private fun nextStep() {
        val nextCell: Maze.Cell
        try {
            nextCell = mazeSolver.next()
        } catch (e: NoFinishCellException) {
            stop(StopReasons.NOFINISH)
            return
        }

        // окрашивает текущую ячейку в данном шаге решения.
        // ячейку BORDER - в серый цвет, ROAD - в желтый цвет, START и FINISH - в зеленый.
        mazeGrid.children.find { (it as CellPane).row == nextCell.row && it.column == nextCell.column }?.style =
            "-fx-background-color:" + when (nextCell.kind) {
                Maze.CellKind.BORDER -> "gray"
                Maze.CellKind.ROAD -> "yellow"
                Maze.CellKind.START -> "green"
                Maze.CellKind.FINISH -> "green"
            }

        // если найден финиш, то завершаем решение
        if (nextCell.kind == Maze.CellKind.FINISH) {
            stop(StopReasons.COMPLETED)
        }
    }

    /**
     * Причины остановки решателя
     *  * COMPLETED - лабиринт пройден
     *  * NOFINISH - отсутствие достигаемости финиша в лабиринте
     */
    private enum class StopReasons {
        COMPLETED, NOFINISH
    }

    /**
     * Останавливает решатель и выдает сообщение пользователю в зависимости от причины остановки решателя
     */
    private fun stop(reason: StopReasons) {
        onPauseButtonClick()
        start.isDisable = true

        increaseSpeed.isDisable = true
        decreaseSpeed.isDisable = true

        Platform.runLater {
            when (reason) {
                StopReasons.COMPLETED -> Alert(Alert.AlertType.INFORMATION).apply {
                    title = "Yeah!"
                    contentText = "The maze is completed!"
                    showAndWait()
                }

                StopReasons.NOFINISH -> Alert(Alert.AlertType.INFORMATION).apply {
                    title = "Oops!"
                    contentText = "The maze does not have any finish!"
                    showAndWait()
                }
            }
        }
    }

    @FXML
    private fun initialize() {
        // Инициализация скорости
        stepSpeed.text = currentSpeed.toString()
        // инициализация лабиринта
        maze = Maze.fromFile(currentMazeConfigFile.absolutePath, mazeMetrics!!)
        // задание ячеек лабиринта на UI
        maze.traverseAll { cell ->
            mazeGrid.add(cell.toView(), cell.column, cell.row)
        }

        mazeSolver = MazeSolver(maze)

        start.isDisable = false
        pause.isDisable = true

    }
}