package io.github.kotlinmania.rayon

/**
 * Error returned when building a [ThreadPool] fails.
 */
public class ThreadPoolBuildError(
    override val message: String,
) : Exception(message)

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
        return io.github.kotlinmania.rayon
            .scope(op)
    }

    public fun <A, B> join(operA: () -> A, operB: () -> B): Pair<A, B> {
        startHandler?.invoke(0)
        return Pair(operA(), operB())
    }
}
