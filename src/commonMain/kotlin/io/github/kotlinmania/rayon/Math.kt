// port-lint: source math.rs
package io.github.kotlinmania.rayon

/**
 * An endpoint bound for a range.
 */
public sealed class Bound<out T> {
    public data class Included<T>(
        public val value: T,
    ) : Bound<T>()

    public data class Excluded<T>(
        public val value: T,
    ) : Bound<T>()

    public object Unbounded : Bound<Nothing>()
}

/**
 * Represents a range with endpoint bounds.
 */
public interface RangeBounds<T> {
    public fun startBound(): Bound<T>

    public fun endBound(): Bound<T>
}

/**
 * A standard half-open range from start to end.
 */
public data class Range<T : Comparable<T>>(
    public val start: T,
    public val end: T,
)

/**
 * Divide `n` by `divisor`, and round up to the nearest integer
 * if not evenly divisible.
 */
public fun divRoundUp(n: ULong, divisor: ULong): ULong {
    require(divisor != 0uL) { "Division by zero!" }
    return if (n == 0uL) {
        0uL
    } else {
        (n - 1uL) / divisor + 1uL
    }
}

/**
 * Divide `n` by `divisor`, and round up to the nearest integer
 * if not evenly divisible.
 */
public fun divRoundUp(n: Int, divisor: Int): Int {
    require(divisor != 0) { "Division by zero!" }
    return if (n == 0) {
        0
    } else {
        (n - 1) / divisor + 1
    }
}

/**
 * Normalize arbitrary range bounds to a range.
 */
public fun simplifyRange(range: RangeBounds<Int>, len: Int): Range<Int> {
    val start =
        when (val bound = range.startBound()) {
            is Bound.Unbounded -> {
                0
            }

            is Bound.Included -> {
                val i = bound.value
                if (i <= len) i else throw IllegalArgumentException("range start $bound should be <= length $len")
            }

            is Bound.Excluded -> {
                val i = bound.value
                if (i < len) i + 1 else throw IllegalArgumentException("range start $bound should be <= length $len")
            }
        }
    val end =
        when (val bound = range.endBound()) {
            is Bound.Unbounded -> {
                len
            }

            is Bound.Excluded -> {
                val i = bound.value
                if (i <= len) i else throw IllegalArgumentException("range end $bound should be <= length $len")
            }

            is Bound.Included -> {
                val i = bound.value
                if (i < len) i + 1 else throw IllegalArgumentException("range end $bound should be <= length $len")
            }
        }
    if (start > end) {
        throw IllegalArgumentException("range start ${range.startBound()} should be <= range end ${range.endBound()}")
    }
    return Range(start, end)
}
