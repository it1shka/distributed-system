import java.awt.Color
import kotlin.math.pow
import kotlin.math.sqrt
import kotlin.random.Random

object Colors {
    const val RESET = "\u001B[0m"
    const val BLACK = "\u001B[30m"
    const val RED = "\u001B[31m"
    const val GREEN = "\u001B[32m"
    const val YELLOW = "\u001B[33m"
    const val BLUE = "\u001B[34m"
    const val PURPLE = "\u001B[35m"
    const val CYAN = "\u001B[36m"
    const val WHITE = "\u001B[37m"
}

inline fun <T> maybe(probability: Int, action: () -> T) =
    if (Random.nextInt(100) < probability)
        action()
    else null

inline fun <A, B> Pair<A, A>.transform(action: (A) -> B) =
    action(first) to action(second)

infix fun Pair<Int, Int>.distanceTo(other: Pair<Int, Int>): Float {
    val (x1, y1) = transform(Int::toFloat)
    val (x2, y2) = other.transform(Int::toFloat)
    return sqrt((x1 - x2).pow(2) + (y1 - y2).pow(2))
}

fun <T> List<T>.allPairs(): List<Pair<T, T>> {
    val output = mutableListOf<Pair<T, T>>()
    for (i in indices) {
        for (j in i until size) {
            output.add(get(i) to get(j))
        }
    }
    return output
}

fun clearTerminal() {
    print("\u001B[H\u001B[2J")
}

object Logger {
    private var ticks = 1
    private val history = MutableList(5) { "" }

    private var migrations = 0
    private var workload = 0
    private var requests = 0

    private var totalMigrations = 0
    private var totalWorkload = 0
    private var totalRequests = 0

    fun registerMigration(from: String, to: String, process: String) {
        history.removeFirstOrNull()
        history.add("Migration from ${Colors.GREEN}$from${Colors.RESET} to ${Colors.RED}$to${Colors.RESET} " +
                "of ${Colors.PURPLE}$process${Colors.RESET}")
        migrations++
        totalMigrations++
    }

    fun registerWorkload(delta: Int) {
        workload += delta
        totalWorkload += delta
    }

    fun registerRequests(req: Int = 1) {
        requests += req
        totalRequests += req
    }

    fun dump() {
        clearTerminal()

        println("${Colors.YELLOW}Migrations: ${Colors.RESET}")
        println(history.joinToString(separator = "\n"))
        println()

        println("${Colors.YELLOW}Migrations: $migrations ${Colors.RESET}")
        println("${Colors.YELLOW}Requests: $requests ${Colors.RESET}")
        println("${Colors.YELLOW}Workload: ${workload.toFloat() / 100.0} ${Colors.RESET}")
        println()

        println("${Colors.CYAN}Mean migrations: ${totalMigrations.toFloat() / ticks} ${Colors.RESET}")
        println("${Colors.CYAN}Mean requests: ${totalRequests.toFloat() / ticks} ${Colors.RESET}")
        println("${Colors.CYAN}Mean workload: ${totalWorkload.toFloat() / ticks / 100.0} ${Colors.RESET}")
        println()

        println("${Colors.GREEN}Total migrations: $totalMigrations ${Colors.RESET}")
        println("${Colors.GREEN}Total requests: $totalRequests ${Colors.RESET}")
        println("${Colors.GREEN}Total workload: ${totalWorkload.toFloat() / 100.0} ${Colors.RESET}")
        println()

        migrations = 0
        requests = 0
        workload = 0
        ticks++
    }

}