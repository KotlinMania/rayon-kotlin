package io.github.kotlinmania.rayon

/**
 * Used to create a new [ThreadPool] or to configure the global rayon thread pool.
 */
public class ThreadPoolBuilder {
    private var numThreads: Int = 0
    private var startHandler: ((Int) -> Unit)? = null
    private var exitHandler: ((Int) -> Unit)? = null
    private var threadName: ((Int) -> String)? = null
    private var stackSize: ULong = 0uL

    public fun numThreads(numThreads: Int): ThreadPoolBuilder {
        this.numThreads = numThreads
        return this
    }

    public fun startHandler(startHandler: (Int) -> Unit): ThreadPoolBuilder {
        this.startHandler = startHandler
        return this
    }

    public fun exitHandler(exitHandler: (Int) -> Unit): ThreadPoolBuilder {
        this.exitHandler = exitHandler
        return this
    }

    public fun threadName(closure: (Int) -> String): ThreadPoolBuilder {
        this.threadName = closure
        return this
    }

    public fun stackSize(stackSize: ULong): ThreadPoolBuilder {
        this.stackSize = stackSize
        return this
    }

    public fun build(): ThreadPoolBuildResult {
        val threads = if (numThreads > 0) numThreads else 1
        return ThreadPoolBuildResult(ThreadPool(threads, startHandler))
    }

    public companion object {
        public fun new(): ThreadPoolBuilder = ThreadPoolBuilder()
    }
}
