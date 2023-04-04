class Computer (
    val label: String,
    val type: String,
    val position: Pair<Int, Int>
) {

    private val newProcessChance = 2
    private val minThreshold = 20
    private val maxThreshold = 90
    private val requestThreshold = 5

    private val processes = mutableListOf<Process>()
    private val network = mutableListOf<Computer>()
    var workload = 0
        private set

    fun update() {
        processes.forEach { it.execute() }
        workload = processes.sumOf { it.workload }
        processes.removeIf { !it.active }
        maybe(newProcessChance) {
            val newProcess = ProcessGenerator.getRandomProcess()
            processes.add(newProcess)
        }
        balance()
    }

    fun dispose() {
        processes.clear()
        network.clear()
    }

    fun forceAddProcess(process: Process) {
        processes.add(process)
    }

    fun connect(computer: Computer) = network.add(computer)

    private fun requestSendProcess(process: Process): Boolean {
        if (workload + process.workload > maxThreshold)
            return false
        processes.add(process)
        return true
    }

    private fun requestReceiveProcess(expectedWorkload: Int): Process? {
        if ((type == "Min" || type == "MinMax") && workload <= minThreshold)
            return null
        val process = processes
            .filter { it.workload <= expectedWorkload }
            .maxByOrNull { it.workload }
        processes.remove(process)
        return process
    }

    private fun getPossibleNeighbors() = network
        .sortedBy { position distanceTo it.position }
        .take(requestThreshold)

    private fun balance() {
        if (type == "Min" || type == "MinMax") {
            balanceMin()
        }
        if (type == "Max" || type == "MinMax") {
            balanceMax()
        }
    }

    private fun balanceMin() {
        if (workload >= minThreshold) return
        val possible = getPossibleNeighbors()
        for (each in possible) {
            val maybeProcess = each.requestReceiveProcess(maxThreshold - workload)
            if (maybeProcess != null) {
                processes.add(maybeProcess)
                break
            }
        }
    }

    private fun balanceMax() {
        if (workload <= maxThreshold) return
        val possible = getPossibleNeighbors()
        for (each in possible) {
            val minimal = processes.minBy { it.workload }
            if (each.requestSendProcess(minimal)) {
                processes.remove(minimal)
                break
            }
        }
    }

}
