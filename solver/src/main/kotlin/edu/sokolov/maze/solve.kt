package edu.sokolov.maze

import java.util.LinkedList
import edu.sokolov.maze.Maze.Cell
import edu.sokolov.maze.Maze.Cell.StartCell
import edu.sokolov.maze.Maze.Cell.FinishCell
import edu.sokolov.maze.Maze.Cell.WallCell
import java.util.Queue


class MazeSolver(val maze: Maze) {


    fun solve() = maze.start.findFinish()

    private var current = maze.start
    private val currentNeighbours: Queue<Cell> = LinkedList()

    private val visited: MutableMap<Cell, Cell?> = hashMapOf()
    private val queue: Queue<Cell> = LinkedList(listOf(current))

    fun next(): Cell {
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
    while (current !is Cell.FinishCell) {
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
/*
public LinkedList<Vertex> breadthFirstSearch(String start, String end) {
    Vertex startVert = vertices.get(start);
    Vertex endVert = vertices.get(end);

    LinkedList<Vertex> queue = new LinkedList<>(); // LinkedList implements Queue
    HashMap<Vertex, Vertex> visited = new HashMap<>();

    visited.put(startVert, null); // this is the first vertex

    Vertex current = startVert; // the current vertex to check
    while (current != endVert) { // repeats until the end is reached

        LinkedList<Vertex> adjacents = current.getAdjacents(); // get adjacents

        for (Vertex v: adjacents) { // add all the adjacents
            if (!visited.containsKey(v)) { // but only if it hasn't already been traversed
                visited.put(v, current);
                queue.add(v);
            }
        }

        current = queue.remove(); // goes to the next vertex
    }

    // create the path
    LinkedList<Vertex> path = new LinkedList<>();
    path.addFirst(current);
    while (current != startVert) {
        current = visited.get(current);
        path.addFirst(current); // addFirst is used to get the correct order
    }

    return path;
}
 */

/*
public void breadthFirstTraversal(String start, String end) {
    Vertex startVert = vertices.get(start);
    Vertex endVert = vertices.get(end);

    LinkedList<Vertex> queue = new LinkedList<>(); // LinkedList implements Queue
    HashSet<Vertex> visited = new HashSet<>();

    visited.add(startVert); // this is the first vertex

    Vertex current = startVert; // the current vertex to check
    while (current != endVert) { // repeats until the end is reached

        LinkedList<Vertex> adjacents = current.getAdjacents(); // get adjacents

        for (Vertex v: adjacents) { // add all the adjacents
            if (!visited.contains(v)) { // but only if it hasn't already been traversed
                visited.add(v);
                queue.add(v);
            }
        }

        current = queue.remove(); // goes to the next vertex
    }
}
 */