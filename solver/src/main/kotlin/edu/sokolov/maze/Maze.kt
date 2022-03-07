package edu.sokolov.maze

import java.io.File
import java.util.*

class Maze private constructor(
    val rows: Int,
    val columns: Int,
    val start: Cell
) {

    sealed class Cell(val row: Int, val column: Int) {

        private val neighbours: EnumMap<Directions, Cell> = EnumMap(Directions::class.java)

        operator fun get(direction: Directions) = neighbours[direction]

        fun addNeighbour(direction: Directions, neighbour: Cell) {
            neighbours[direction] = neighbour
        }


        class RoadCell(row: Int, column: Int): Cell(row, column) {

        }

        class BorderCell(row: Int, column: Int): Cell(row, column) {

        }

        class StartCell(row: Int, column: Int): Cell(row, column) {

        }

        class FinishCell(row: Int, column: Int): Cell(row, column) {

        }
    }

    enum class Directions {
        LEFT, RIGHT, UP, DOWN
    }

    companion object {

        const val ROAD_CELL = '0'
        const val BORDER_CELL = '1'
        const val START_CELL = 'S'
        const val FINISH_CELL = 'F'

        private fun Char.toCell(row: Int, column: Int): Cell = when(this) {
            ROAD_CELL -> Cell.RoadCell(row, column)
            BORDER_CELL -> Cell.BorderCell(row, column)
            START_CELL -> Cell.StartCell(row, column)
            FINISH_CELL -> Cell.FinishCell(row, column)
            else -> throw IllegalArgumentException("this char [$this] is not compatible with cells mapping!")
        }

        fun fromFile(filename: String): Maze {
            val file = File(filename)

            val rawText = file.readLines()

            for (line in 1..rawText.lastIndex) {
                if (rawText[line - 1].length != rawText[line].length)
                    throw InvalidMazeSizeException()
            }

            val rows = rawText.size
            val columns = rawText.first().length

            var startCell: Cell.StartCell? = null
            var startCells = 0
            var finishCells = 0

            // create cells table
            val cells = Array(rows) { row ->
                Array(columns) { column ->
                    rawText[row][column].toCell(row, column).apply {
                        when (this) {
                            is Cell.StartCell -> { startCells++; startCell = this }
                            is Cell.FinishCell -> finishCells++
                        }
                    }
                }
            }

            if (startCells != 1 || finishCells != 1)
                throw InvalidMazeDoorsCountException()

            // set neighbours for each cell
            for (row in 0 until rows) {
                for (column in 0 until columns) {
                    if (row > 0) cells[row][column].addNeighbour(Directions.UP, cells[row - 1][column])
                    if (row < rows - 1) cells[row][column].addNeighbour(Directions.DOWN, cells[row + 1][column])
                    if (column > 0) cells[row][column].addNeighbour(Directions.LEFT, cells[row][column - 1])
                    if (column < columns - 1) cells[row][column].addNeighbour(Directions.RIGHT, cells[row][column + 1])
                }
            }

            return Maze(rows, columns, startCell!!)
        }
    }
}

open class InvalidMazeFileFormatException() : Exception()
class InvalidMazeSizeException() : InvalidMazeFileFormatException()
class InvalidMazeDoorsCountException() : InvalidMazeFileFormatException()