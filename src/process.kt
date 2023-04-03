import kotlin.random.Random
import kotlin.random.nextInt

class Process (
    private val name: String,
    private var lifetime: Int,
    private val averageLoad: Int,
    private val loadRange: Int = 10
) {
    private var currentLoad = 0
    val active get() = lifetime > 0

    fun execute(): Int {
        if (!active) return 0
        currentLoad =  averageLoad + Random.nextInt(-loadRange .. loadRange)
        lifetime--
        return currentLoad
    }

    override fun toString(): String {
        return "$name ($currentLoad)"
    }
}

object ProcessGenerator {
    private val names = arrayOf("virus", "app", "script", "main", "server", "daemon", "node", "program")
    private val extensions = arrayOf(".js", ".py", ".exe", ".rb", ".sh", ".exs", ".jl")

    private fun getRandomName(): String {
        val name = names.random()
        val index = Random.nextInt(10)
        val extension = extensions.random()
        return "$name$index$extension"
    }

    private const val maxLoad = 30
    private const val maxLoadRange = 10
    private const val minLifetime = 20
    private const val maxLifetime = 120

    fun getRandomProcess(): Process {
        val name = getRandomName()
        val load = Random.nextInt(maxLoad)
        val range = Random.nextInt(maxLoadRange)
        val lifetime = Random.nextInt(minLifetime .. maxLifetime)
        return Process(name, lifetime, load, range)
    }
}