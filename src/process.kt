import kotlin.random.Random
import kotlin.random.nextInt

class Process (
    val name: String,
    private var lifetime: Int,
    private val averageWorkload: Int,
    private val workloadRange: Int = 10
) {
    var workload: Int = 0
        private set

    val active get() = lifetime > 0

    fun execute() {
        if (!active) return
        val currentRange = Random.nextInt(-workloadRange .. workloadRange)
        workload =  averageWorkload + currentRange
        lifetime--
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

    private val load = 5..30
    private val loadRange = 1..10
    private val lifetime = 20..120

    fun getRandomProcess(): Process {
        val name = getRandomName()
        val load = Random.nextInt(load)
        val range = Random.nextInt(loadRange)
        val lifetime = Random.nextInt(lifetime)
        return Process(name, lifetime, load, range)
    }
}