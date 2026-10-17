package io.github.kotlinmania.rayon

/**
 * Takes two closures and potentially runs them in parallel.
 */
public fun <A, B> join(operA: () -> A, operB: () -> B): Pair<A, B> = Pair(operA(), operB())

public fun currentNumThreads(): Int = 1

public fun currentThreadIndex(): Int? = 0

public fun maxNumThreads(): Int = 64
