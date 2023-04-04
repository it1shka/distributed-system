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