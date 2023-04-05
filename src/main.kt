import java.awt.Color
import java.awt.Dimension
import java.awt.Graphics
import java.awt.event.*
import javax.swing.JFrame
import javax.swing.WindowConstants

object Window: JFrame("Network") {

    private const val computerSize = 30
    private val computerTypes = arrayOf("Min", "Max", "MinMax")
    private var chosenComputerTypeIndex = 0
    private var computerIndex = 1
    private val currentComputerType get() =
        computerTypes[chosenComputerTypeIndex % computerTypes.size]
    private var mousePosition = 0 to 0
    private val computers = mutableListOf<Computer>()

    init {
        size = Dimension(600, 600)
        defaultCloseOperation = WindowConstants.DO_NOTHING_ON_CLOSE
        isVisible = true
        // binding controls
        addMouseMotionListener(object: MouseMotionAdapter() {
            override fun mouseMoved(e: MouseEvent?) {
                if (e == null) return
                mousePosition = e.x to e.y
            }
        })
        addMouseListener(object: MouseAdapter() {
            override fun mouseClicked(e: MouseEvent?) {
                if (e == null) return
                click()
            }
        })
        addKeyListener(object: KeyAdapter() {
            override fun keyPressed(e: KeyEvent?) {
                when (e?.keyCode) {
                    KeyEvent.VK_T -> chosenComputerTypeIndex++
                    KeyEvent.VK_Z -> {
                        val maybeComputer = computers.removeLastOrNull()
                        maybeComputer?.dispose()
                    }
                    KeyEvent.VK_C -> {
                        computers.forEach { it.dispose() }
                        computers.clear()
                    }
                    KeyEvent.VK_A -> {
                        val randomProcess = ProcessGenerator.getRandomProcess()
                        computers.randomOrNull()?.forceAddProcess(randomProcess)
                    }
                }
            }
        })
    }

    private fun click() {
        val name = "Computer ${computerIndex++}"
        val computer = Computer(name, currentComputerType, mousePosition)
        for (each in computers) {
            computer.connect(each)
            each.connect(computer)
        }
        computers.add(computer)
    }

    private fun colorOfComputer(computer: String) = when(computer) {
        "Min" -> Color(0, 0, 204)
        "Max" -> Color(204, 153, 0)
        "MinMax" -> Color(102, 255, 153)
        else -> Color.GRAY
    }

    override fun paint(g: Graphics?) {
        if (g == null) return

        g.color = Color.white
        g.fillRect(0, 0, width, height)

        g.color = Color.red
        val (mouseX, mouseY) = mousePosition
        drawComputer(g, mouseX, mouseY, "Future", currentComputerType)

        for ((a, b) in computers.allPairs()) {
            g.color = colorOfComputer(b.type)
            val (x1, y1) = a.position
            val (x2, y2) = b.position
            g.drawLine(x1, y1, x2, y2)
        }

        for (each in computers) {
            drawComputer(g, each)
        }
    }

    private fun drawComputer(g: Graphics, x: Int, y: Int, name: String, type: String) {
        g.color = colorOfComputer(type)
        g.fillRect(
            x - computerSize/2,
            y - computerSize/2,
            computerSize,
            computerSize
        )
        g.drawString(type, x - computerSize/2, y + computerSize)

        g.color = Color.black
        g.drawString(name, x - computerSize/2, y - computerSize + 10)
    }

    private fun drawComputer(g: Graphics, computer: Computer) {
        val (x, y) = computer.position
        drawComputer(g, x, y, computer.label, computer.type)
        g.drawString("${computer.workload}%", x + 20, y + 5)
    }

    fun update() = computers.forEach { it.update() }

}

fun main() {
    while (true) {
        Window.update()
        Window.repaint()
        Logger.dump()
        Thread.sleep(50)
    }
}