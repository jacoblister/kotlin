@Suppress("PLATFORM_CLASS_MAPPED_TO_KOTLIN")
class CubbyHole {
    private var contents: Int = -1
    private var available: Boolean = false

    @Synchronized
    fun get(): Int {
        while (!available) {
            try {
                (this as Object).wait() // Wait until a value is available
            } catch (e: InterruptedException) {
                Thread.currentThread().interrupt()
                println("Consumer interrupted: ${e.message}")
            }
        }
        available = false
        (this as Object).notifyAll() // Notify all waiting threads (e.g., producer)
        return contents
    }

    @Synchronized
    fun put(value: Int) {
        while (available) {
            try {
                (this as Object).wait() // Wait until the current value is consumed
            } catch (e: InterruptedException) {
                Thread.currentThread().interrupt()
                println("Producer interrupted: ${e.message}")
            }
        }
        contents = value
        available = true
        (this as Object).notifyAll() // Notify all waiting threads (e.g., consumer)
    }
}

fun main() {
    val cubbyHole = CubbyHole()

    val producerThread = Thread {
        for (i in 1..5) {
            cubbyHole.put(i)
            println("Producer put: $i")
            Thread.sleep(100) // Simulate some work
        }
    }

    val consumerThread1 = Thread {
        for (i in 1..3) {
            val value = cubbyHole.get()
            println("Consumer 1 got: $value")
            Thread.sleep(150) // Simulate some work
        }
    }

    val consumerThread2 = Thread {
        for (i in 1..2) {
            val value = cubbyHole.get()
            println("Consumer 2 got: $value")
            Thread.sleep(200) // Simulate some work
        }
    }

    producerThread.start()
    consumerThread1.start()
    consumerThread2.start()

//    Thread.sleep(5000)

    println("Main thread finished.")
}