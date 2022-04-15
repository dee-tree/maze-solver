package edu.sokolov.maze

/**
 * Матрика, оценивающее значение ячейки `current` относительно ячейки финиша `finish`
 */
interface Metrics {
    fun evaluate(current: Maze.Cell, finish: Maze.Cell): Int
}

/**
 * Метрика, реализующая Манхэттенское расстояние. Возвращает сумму квадратов расстояния по каждой оси от
 * ячейки `current` до ячейки `finish`.
 * Для простоты вычислений корень из суммы не извлекается.
 *
 * Данная метрика позволяет реализовать решение лабиринта по наиболее близкому пути к финишу (A*)
 */
class SquaredManhattan : Metrics {
    override fun evaluate(current: Maze.Cell, finish: Maze.Cell): Int {
        val rowsDistance = finish.row - current.row
        val columnsDistance = finish.column - current.column
        return rowsDistance * rowsDistance + columnsDistance * columnsDistance
    }
}

/**
 * Константная метрика. Возвращает 0 для любой пары `current` и `finish`, не выделяя приоритет ни для какой из них.
 *
 * Данная метрика позволяет реализовать решение лабиринта на основе поиска в ширину.
 */
class ConstMetrics : Metrics {
    override fun evaluate(current: Maze.Cell, finish: Maze.Cell): Int = 0
}