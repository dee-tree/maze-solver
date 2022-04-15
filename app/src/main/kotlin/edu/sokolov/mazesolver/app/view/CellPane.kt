package edu.sokolov.mazesolver.app.view

import edu.sokolov.maze.Maze
import javafx.geometry.Pos
import javafx.scene.control.Label
import javafx.scene.layout.StackPane

/**
 * Класс, описывающий визуальное представление ячейки на основе StackPane
 */
sealed class CellPane(val row: Int, val column: Int) : StackPane() {
    abstract val cell: Maze.Cell


    class WallCellPane(override val cell: Maze.Cell) : CellPane(cell.row, cell.column) {
        init {
            val text = label("1")
            super.getChildren().add(text)
        }
    }

    class RoadCellPane(override val cell: Maze.Cell) : CellPane(cell.row, cell.column) {
        init {
            val text = label("0")
            super.getChildren().add(text)
        }
    }

    class StartCellPane(override val cell: Maze.Cell) : CellPane(cell.row, cell.column) {
        init {
            val text = label("S")
            super.getChildren().add(text)
        }
    }

    class FinishCellPane(override val cell: Maze.Cell) : CellPane(cell.row, cell.column) {
        init {
            val text = label("F")
            super.getChildren().add(text)
        }
    }

}

fun Maze.Cell.toView(): CellPane = when (this.kind) {
    Maze.CellKind.ROAD -> CellPane.RoadCellPane(this)
    Maze.CellKind.BORDER -> CellPane.WallCellPane(this)
    Maze.CellKind.START -> CellPane.StartCellPane(this)
    Maze.CellKind.FINISH -> CellPane.FinishCellPane(this)
}


private fun label(text: String) = Label(text).apply {
    alignment = Pos.CENTER
    minWidth = 30.0
    minHeight = 30.0
}
