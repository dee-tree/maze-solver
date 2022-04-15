package edu.sokolov.maze

import edu.sokolov.maze.Maze.Cell
import java.util.*

/**
 * Класс, описывающий решатель лабиринта `maze`
 */
class MazeSolver(val maze: Maze) {


    /**
     * Решает лабиринт (т.е. находит путь от старта до финиша), возвращает список из `Cell`, лежащих на этом пути
     */
    fun solve() = maze.start.findFinish()

    private lateinit var current: Cell
    private val currentNeighbours = PriorityQueue<Cell>(compareBy { it.measureMetrics() })

    private val visited: MutableMap<Cell, Cell?> = hashMapOf()
    private val queue = PriorityQueue<Cell>(compareBy { it.measureMetrics() })

    /**
     * Возвращает ячейку, которая является следующей при проходе по лабиринту
     */
    fun next(): Cell {
        if (!this::current.isInitialized) {
            current = maze.start
            queue.add(maze.start)
            return current
        }
        visited[current] = null

        // Проверка, найден ли финиш
        if (current.kind != Maze.CellKind.FINISH) {
            // заполнение всех текущих соседей
            if (currentNeighbours.isEmpty()) {
                currentNeighbours.addAll(current.allNeighbours)
            }

            // Получение самого приоритетного соседа
            val neighbour = currentNeighbours.poll()
            if (!visited.containsKey(neighbour) && neighbour.kind != Maze.CellKind.BORDER) {
                visited[neighbour] = current
                queue.add(neighbour)
            }

            if (currentNeighbours.isNotEmpty())
                return neighbour

            if (queue.isEmpty())
                throw NoFinishCellException()
            current = queue.poll()
        }

        return current
    }

    fun path(): Queue<Cell> {
        val path = LinkedList<Cell>()
        path.addFirst(current)
        while (current.kind != Maze.CellKind.START) {
            current = visited[current]!!
            path.addFirst(current)
        }
        return path
    }
}

fun Cell.findFinish(): List<Cell> {
    val queue = PriorityQueue<Cell>(compareBy { it.measureMetrics() })
    val visited: MutableMap<Cell, Cell?> = hashMapOf()

    visited[this] = null

    var current = this
    while (current.kind != Maze.CellKind.FINISH) {
        val neighbours = current.allNeighbours

        // Обход соседей
        for (neighbour in neighbours.sortedBy { it.measureMetrics() }) {
            if (!visited.containsKey(neighbour) && neighbour.kind != Maze.CellKind.BORDER) {
                visited[neighbour] = current
                queue.add(neighbour)
            }
        }

        if (queue.isEmpty())
            throw NoFinishCellException()
        current = queue.poll()
    }

    // восстановление пройденного пути
    val path = LinkedList<Cell>()
    path.addFirst(current)
    while (current != this) {
        current = visited[current]!!
        path.addFirst(current)
    }
    return path
}

class NoFinishCellException() : MazeException()