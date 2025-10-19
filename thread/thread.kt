fun main() {
    val myThread = Thread {
        println("Thread running in: ${Thread.currentThread().name}")
        // Perform some long-running task
        Thread.sleep(2000) // Simulate work
        println("Thread finished in: ${Thread.currentThread().name}")
    }
    myThread.name = "MyCustomThread"
    myThread.start() // Start the thread

    println("Main thread continues in: ${Thread.currentThread().name}")
    Thread.sleep(3000)
    println("Done")
}