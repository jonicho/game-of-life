package de.jonicho.gameoflife

import kotlin.random.Random

class GameOfLife(private val width: Int = 100, private val height: Int = 100) {
    private var world: Array<BooleanArray> =
            Array(width) { BooleanArray(height) { Random.nextBoolean() } }
        set(value) {
            require(value.size == width && value.all { it.size == height }) {
                "World must be have a width of $width and a height of $height."
            }
            field = value
        }

    private var newWorld: Array<BooleanArray> = Array(width) { BooleanArray(height) }
        set(value) {
            require(value.size == width && value.all { it.size == height }) {
                "World must be have a width of $width and a height of $height."
            }
            field = value
        }

    init {
        require(width > 0 && height > 0) { "Width and height must be greater than 0." }
    }

    operator fun get(x: Int, y: Int) = world[x, y]

    operator fun set(x: Int, y: Int, cell: Boolean) {
        world[x, y] = cell
    }

    private operator fun Array<BooleanArray>.get(x: Int, y: Int) = this[x][y]

    private operator fun Array<BooleanArray>.set(x: Int, y: Int, cell: Boolean) {
        this[x][y] = cell
    }

    private fun getAliveNeighbors(x: Int, y: Int): Int {
        require(x in 0 until width && y in 0 until height)
        var count = 0
        for (x1 in x - 1..x + 1) {
            for (y1 in y - 1..y + 1) {
                if (x1 == x && y1 == y) continue
                // wrap around the other side
                val x2 = (width + x1) % width
                val y2 = (height + y1) % height
                if (world[x2, y2]) count++
            }
        }
        return count
    }

    fun step() {
        for (x in 0 until width) {
            for (y in 0 until height) {
                val aliveNeighbors = getAliveNeighbors(x, y)
                newWorld[x, y] =
                        if (world[x, y]) {
                            when (aliveNeighbors) {
                                0, 1 -> false // dies because of underpopulation
                                2, 3 -> true // stays alive
                                in 3..8 -> false // dies because of overpopulation
                                else ->
                                        throw IllegalStateException(
                                                "aliveNeighbors should be between 0 and 8 but is $aliveNeighbors")
                            }
                        } else {
                            when (aliveNeighbors) {
                                3 -> true // becomes alive because of reproduction
                                in 0..8 -> false // stays dead
                                else ->
                                        throw IllegalStateException(
                                                "aliveNeighbors should be between 0 and 8 but is $aliveNeighbors")
                            }
                        }
            }
        }
        world = newWorld.also { newWorld = world }
    }
}
