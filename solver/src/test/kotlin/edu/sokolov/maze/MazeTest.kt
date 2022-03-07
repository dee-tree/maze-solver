package edu.sokolov.maze

import org.jetbrains.annotations.TestOnly
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import java.io.File
import kotlin.test.assertFailsWith

class MazeTest {


    @Test
    fun `test sizes`() {
        val input = File("input").apply {
            writeText(
                """
                1111
                1101
                11011
                SF01
            """.trimIndent()
            )
        }

        assertFailsWith<InvalidMazeSizeException> {
            Maze.fromFile(input.absolutePath)
        }

        input.writeText(
            """
                1111
                11101
                11011
                SF01
            """.trimIndent()
        )

        assertFailsWith<InvalidMazeSizeException> {
            Maze.fromFile(input.absolutePath)
        }

        input.writeText(
            """
                11111
                11101
                11011
                SF011
                1
            """.trimIndent()
        )

        assertFailsWith<InvalidMazeSizeException> {
            Maze.fromFile(input.absolutePath)
        }


        input.writeText(
            """
                11111
                11101
                11011
                SF011
                11111
            """.trimIndent()
        )

        assertDoesNotThrow {
            Maze.fromFile(input.absolutePath)
        }

        input.delete()
    }

    @Test
    fun `test starts and stops`() {
        val input = File("input").apply {
            writeText(
                """
                1111
                1101
                1101
                0001
            """.trimIndent()
            )
        }

        assertFailsWith<InvalidMazeDoorsCountException>(message = "Lack of start and finish") {
            Maze.fromFile(input.absolutePath)
        }

        input.writeText(
            """
                1111
                1101
                1101
                0S01
            """.trimIndent()
        )

        assertFailsWith<InvalidMazeDoorsCountException>(message = "Lack of finish") {
            Maze.fromFile(input.absolutePath)
        }

        input.writeText(
            """
                1111
                1101
                1101
                0F01
            """.trimIndent()
        )

        assertFailsWith<InvalidMazeDoorsCountException>(message = "Lack of start") {
            Maze.fromFile(input.absolutePath)
        }

        input.writeText(
            """
                1111
                1S01
                110S
                0F01
            """.trimIndent()
        )

        assertFailsWith<InvalidMazeDoorsCountException>(message = "Multiple starts") {
            Maze.fromFile(input.absolutePath)
        }

        input.writeText(
            """
                1111
                1F01
                110S
                0F01
            """.trimIndent()
        )

        assertFailsWith<InvalidMazeDoorsCountException>(message = "Multiple finishes") {
            Maze.fromFile(input.absolutePath)
        }

        input.delete()
    }
}