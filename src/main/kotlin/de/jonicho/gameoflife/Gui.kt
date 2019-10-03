package de.jonicho.gameoflife

import java.awt.Color
import java.awt.Graphics
import java.awt.Graphics2D
import javax.swing.JFrame
import javax.swing.JPanel

class Gui(private val width: Int = 300, private val height: Int = 200, private val cellSize: Int = 5) {
    private val frame: JFrame = JFrame()
    private val game = GameOfLife(width, height)
    private val canvas = object : JPanel() {
        override fun paintComponent(gg: Graphics) {
            val g = gg as Graphics2D
            for (x in 0 until this@Gui.width) {
                for (y in 0 until this@Gui.height) {
                    g.color = if (game[x, y]) Color.BLACK else Color.WHITE
                    g.fillRect(x * cellSize, y * cellSize, cellSize, cellSize)
                }
            }
        }
    }

    init {
        frame.add(canvas)
        frame.pack()
        frame.setSize(width * cellSize, height * cellSize + frame.insets.top)
        frame.setLocationRelativeTo(null)
        frame.defaultCloseOperation = JFrame.EXIT_ON_CLOSE
        frame.isVisible = true

        Thread {
            while (true) {
                Thread.sleep(50)
                game.step()
                canvas.repaint()
            }
        }.start()
    }

}