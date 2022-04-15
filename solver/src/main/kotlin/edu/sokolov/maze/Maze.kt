package edu.sokolov.maze

import java.io.File
import java.util.*

/**
 * Класс, описывающий хранение структуры лабиринта.
 * rows - количество строк в лабиринте
 * columns - количество столбцов в лабиринте
 * metrics - матрика оценивания приоритетности путей (или дальности от финиша) для конкретной ячейки
 * maze - структура лабиринта. Двумерный массив размера rows x columns
 */
class Maze private constructor(
    val rows: Int,
    val columns: Int,
    private val metrics: Metrics,
    maze: Array<Array<CellKind>>
) {

    /**
     * свойство maze используется для хранения внутренней структуры лабиринта
     */
    private val maze: Array<Array<Cell>> = maze.mapIndexed { row, rowCells ->
        rowCells.mapIndexed { column, cellKind ->
            Cell(cellKind, row, column)
        }.toTypedArray()
    }.toTypedArray()


    /**
     * Ячейка, представляющая начало лабиринта
     */
    val start: Cell = this.maze.deepFind { it.kind == CellKind.START }!!

    /**
     * Ячейка, представляющая конец (финиш) лабиринта
     */
    val finish: Cell = this.maze.deepFind { it.kind == CellKind.FINISH }!!

    /**
     * Получение соседней ячейки для ячейки `this` со стороны `direction`
     */
    private fun Cell.getNeighbour(direction: Directions): Cell? {
        return when {
            direction == Directions.RIGHT && column < columns - 1 -> maze[row][column + 1]
            direction == Directions.LEFT && column > 0 -> maze[row][column - 1]
            direction == Directions.DOWN && row < rows - 1 -> maze[row + 1][column]
            direction == Directions.UP && row > 0 -> maze[row - 1][column]
            else -> null
        }
    }

    /**
     * Описание ячейки лабиринта
     * kind - тип ячейки
     * row - строка лабиринта, в которой находится данная ячейка
     * row - колонка лабиринта, в которой находится данная ячейка
     */
    inner class Cell(val kind: CellKind, val row: Int, val column: Int) {

        /**
         * Возвращает соседнюю ячейку со стороны `direction`
         */
        operator fun get(direction: Directions): Cell? = getNeighbour(direction)

        /**
         * Возвращает все соседние ячейки.
         * Соседней является ячейка, располагающаяся либо сверху, либо снизу, либо слева, либо справа от текущей
         */
        val allNeighbours: List<Cell>
            get() = Directions.values().fold(emptyList()) { acc, current -> this[current]?.let { acc + it } ?: acc }

        /**
         * Возвращает значение матрики, подсчитанное для данной ячейки относительно финиша лабиринта
         */
        fun measureMetrics(): Int = metrics.evaluate(this, finish)

        override fun toString(): String = "${this::class.simpleName}(row=${row}, column=${column})"
    }

    /**
     * Представление направлений
     */
    enum class Directions {
        LEFT, RIGHT, UP, DOWN
    }

    /**
     * Представление типов ячеек:
     *  * ROAD - ячейка, по которой может осуществляться движение
     *  * BORDER - ячейка, по которой движение запрещено
     *  * START - ячейка, обозначающая старт лабиринта. Движение возможно
     *  * STOP - ячейка, обозначающая финиш лабиринта. Движение возможно
     */
    enum class CellKind {
        ROAD, BORDER, START, FINISH
    }

    companion object {
        const val ROAD_CELL = '0'
        const val BORDER_CELL = '1'
        const val START_CELL = 'S'
        const val FINISH_CELL = 'F'

        /**
         * Преобразование символа `this` возможного описания типа ячейки лабиринта к `CellKind`
         */
        private fun Char.toCellKind(): CellKind = when(this) {
            ROAD_CELL -> CellKind.ROAD
            BORDER_CELL -> CellKind.BORDER
            START_CELL -> CellKind.START
            FINISH_CELL -> CellKind.FINISH
            else -> throw InvalidDataException()
        }

        /**
         * Возвращает лабиринт `Maze`, считанный из файла с именем `filename` и оценочной метрикой `metrics`
         * @throws InvalidMazeDoorsCountException::class
         * @throws InvalidMazeSizeException::class, InvalidDataException::class
         * @throws InvalidDataException::class
         *
         * Возможное представление лабиринта:
         *  * n строк по m символов в каждой строке. Допустимые символы: [0, 1, S, F]
         *  0 -> CellKind.ROAD
         *  1 -> CellKind.BORDER
         *  S -> CellKind.START
         *  F -> CellKind.FINISH
         *
         */
        @Throws(InvalidMazeDoorsCountException::class, InvalidMazeSizeException::class, InvalidDataException::class)
        fun fromFile(filename: String, metrics: Metrics): Maze {
            val file = File(filename)
            // считываем содержимое файла как String
            val rawText = file.readLines()

            // Проверка валиндости размерности лабиринта
            for (line in 1..rawText.lastIndex) {
                if (rawText[line - 1].length != rawText[line].length)
                    throw InvalidMazeSizeException()
            }

            val rows = rawText.size
            val columns = rawText.first().length

            var startCells = 0
            var finishCells = 0

            // Считывание символов и преобразование их в двумерный массив `CellKind` для создания экземпляра `Maze`
            // Подсчет количества ячеек старта и финиша
            val cells = Array(rows) { row ->
                Array(columns) { column ->
                    rawText[row][column].toCellKind().also {
                        when (it) {
                            CellKind.START -> startCells++
                            CellKind.FINISH -> finishCells++
                        }
                    }
                }
            }

            // Проверка соответствия того, что лабиринт имеет только 1 старт и 1 финиш
            if (startCells != 1 || finishCells != 1)
                throw InvalidMazeDoorsCountException()

            return Maze(rows, columns, metrics, cells)
        }
    }

    /**
     * Об
     */
    fun traverseAll(action: (Cell) -> Unit) {
        action(start)
        start.traverse(action)

    }

    private fun Cell.traverse(action: (Cell) -> Unit) {
        val queue = LinkedList<Cell>()
        val visited: MutableMap<Cell, Cell?> = hashMapOf()

        visited[this] = null

        var current = this
        while (visited.size != rows * columns) {
            val neighbours = current.allNeighbours

            for (neighbour in neighbours) {
                if (!visited.containsKey(neighbour)) {
                    visited[neighbour] = current
                    queue.add(neighbour)
                    action(neighbour)
                }
            }

            if (queue.isEmpty())
                throw NoFinishCellException()
            current = queue.remove()
        }
    }

}

/**
 * Возвращает значение, найденное по предикату `condition` в двумерном массиве `this`
 */
private fun <T> Array<Array<T>>.deepFind(condition: (T) -> Boolean): T? {
    forEach { row ->
        row.forEach {
            if (condition(it)) return it
        }
    }
    return null
}

/**
 * Исключение, общее для некорректной конфигурации лабиринта
 */
open class MazeException() : Exception()

open class InvalidMazeFileFormatException() : MazeException()
class InvalidMazeSizeException() : InvalidMazeFileFormatException()
class InvalidDataException() : InvalidMazeFileFormatException()
class InvalidMazeDoorsCountException() : InvalidMazeFileFormatException()