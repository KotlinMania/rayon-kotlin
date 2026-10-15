// port-lint: source prelude.rs
package io.github.kotlinmania.rayon

/**
 * Parallel slice and iterator extensions matching `rayon::prelude`.
 */
public fun <T> List<T>.parIter(): Sequence<T> = asSequence()

public fun <T> Iterable<T>.intoParIter(): Sequence<T> = asSequence()

public fun IntRange.intoParIter(): Sequence<Int> = asSequence()

public fun <T> List<T>.parChunks(chunkSize: Int): List<List<T>> = chunked(chunkSize)

public fun <T> List<T>.parChunksExact(chunkSize: Int): List<List<T>> {
    require(chunkSize > 0)
    val fullChunks = size / chunkSize
    return List(fullChunks) { i -> subList(i * chunkSize, (i + 1) * chunkSize) }
}
