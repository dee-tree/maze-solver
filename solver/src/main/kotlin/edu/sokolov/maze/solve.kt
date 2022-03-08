package edu.sokolov.maze

import java.util.LinkedList
import edu.sokolov.maze.Maze.Cell
import edu.sokolov.maze.Maze.Cell.StartCell
import edu.sokolov.maze.Maze.Cell.FinishCell
import edu.sokolov.maze.Maze.Cell.WallCell
import java.util.Queue


class MazeSolver(val maze: Maze) {


    fun solve() = maze.start.findFinish()

    private lateinit var current: Cell
    private val currentNeighbours: Queue<Cell> = LinkedList()

    private val visited: MutableMap<Cell, Cell?> = hashMapOf()
    private val queue: Queue<Cell> = LinkedList()

    fun next(): Cell {
        if (!this::current.isInitialized) {
            current = maze.start
            queue.add(maze.start)
            return current
        }
        visited[current] = null

        if (current !is FinishCell) {
            if (currentNeighbours.isEmpty()) {
                currentNeighbours.addAll(current.allNeighbours)
            }

            val neighbour = currentNeighbours.remove()
            if (!visited.containsKey(neighbour) && neighbour !is WallCell) {
                visited[neighbour] = current
                queue.add(neighbour)
            }

            if (currentNeighbours.isNotEmpty())
                return neighbour

            if (queue.isEmpty())
                throw NoFinishCellException()
            current = queue.remove()
        }

        return current
    }

    fun path(): Queue<Cell> {
        val path = LinkedList<Cell>()
        path.addFirst(current)
        while (current !is StartCell) {
            current = visited[current]!!
            path.addFirst(current)
        }
        return path
    }
}

fun Cell.findFinish(): List<Cell> {
    val queue = LinkedList<Cell>()
    val visited: MutableMap<Cell, Cell?> = hashMapOf()

    visited[this] = null

    var current = this
    while (current !is FinishCell) {
        val neighbours = current.allNeighbours

        for (neighbour in neighbours) {
            if (!visited.containsKey(neighbour) && neighbour !is WallCell) {
                visited[neighbour] = current
                queue.add(neighbour)
            }
        }

        if (queue.isEmpty())
            throw NoFinishCellException()
        current = queue.remove()
    }

    val path = LinkedList<Cell>()
    path.addFirst(current)
    while (current != this) {
        current = visited[current]!!
        path.addFirst(current)
    }
    return path
}

class NoFinishCellException() : MazeException()