// port-lint: source lib.rs
package io.github.kotlinmania.rayon

/**
 * Error returned when building a [ThreadPool] fails.
 */
public class ThreadPoolBuildError(override val message: String) : Exception(message)

/**
 * Result wrapper for [ThreadPoolBuilder.build] supporting Rust-like `expect` and `unwrap`.
 */
public class ThreadPoolBuildResult(
    private val pool: ThreadPool?,
    private val error: ThreadPoolBuildError? = null,
) {
    public fun isOk(): Boolean = pool != null
    public fun isErr(): Boolean = error != null

    public fun unwrap(): ThreadPool = pool ?: throw (error ?: ThreadPoolBuildError("ThreadPool build failed"))

    public fun expect(msg: String): ThreadPool =
        pool ?: throw ThreadPoolBuildError("$msg: ${error?.message ?: "unknown error"}")
}

/**
 * Represents a user-created Rayon thread-pool.
 */
public class ThreadPool internal constructor(
    private val numThreads: Int,
    private val startHandler: ((Int) -> Unit)? = null,
) {
    public fun currentNumThreads(): Int = numThreads

    public fun <R> install(op: () -> R): R {
        startHandler?.invoke(0)
        return op()
    }

    public fun <R> scope(op: (Scope) -> R): R {
        startHandler?.invoke(0)
        return io.github.kotlinmania.rayon.scope(op)
    }

    public fun <A, B> join(operA: () -> A, operB: () -> B): Pair<A, B> {
        startHandler?.invoke(0)
        return Pair(operA(), operB())
    }
}

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

/**
 * Represents a fork-join scope which can be used to spawn any number of tasks.
 */
public class Scope {
    public fun spawn(body: (Scope) -> Unit) {
        body(this)
    }
}

/**
 * Creates a fork-join scope `s` and invokes the closure with a reference to `s`.
 */
public fun <R> scope(op: (Scope) -> R): R = op(Scope())

/**
 * Creates a fork-join scope `s` and executes `op` in-place.
 */
public fun <R> inPlaceScope(op: (Scope) -> R): R = op(Scope())

/**
 * Takes two closures and potentially runs them in parallel.
 */
public fun <A, B> join(operA: () -> A, operB: () -> B): Pair<A, B> = Pair(operA(), operB())

public fun currentNumThreads(): Int = 1

public fun currentThreadIndex(): Int? = 0

public fun maxNumThreads(): Int = 64

public object rayon {
    public val ThreadPoolBuilder: io.github.kotlinmania.rayon.ThreadPoolBuilder.Companion =
        io.github.kotlinmania.rayon.ThreadPoolBuilder.Companion

    public fun <R> scope(op: (Scope) -> R): R = io.github.kotlinmania.rayon.scope(op)

    public fun <A, B> join(operA: () -> A, operB: () -> B): Pair<A, B> =
        io.github.kotlinmania.rayon.join(operA, operB)

    public fun currentNumThreads(): Int = io.github.kotlinmania.rayon.currentNumThreads()
}
