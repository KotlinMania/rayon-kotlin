// port-lint: tests math.rs
package io.github.kotlinmania.rayon

import kotlin.test.Test
import kotlin.test.assertEquals

class MathTest {
    @Test
    fun checkDivRoundUp() {
        assertEquals(0, divRoundUp(0, 5))
        assertEquals(1, divRoundUp(5, 5))
        assertEquals(1, divRoundUp(1, 5))
        assertEquals(2, divRoundUp(3, 2))
        assertEquals(
            (ULong.MAX_VALUE / 2uL) + 1uL,
            divRoundUp(ULong.MAX_VALUE, 2uL),
        )
    }

    @Test
    fun testSimplifyRange() {
        val range =
            object : RangeBounds<Int> {
                override fun startBound(): Bound<Int> = Bound.Included(2)

                override fun endBound(): Bound<Int> = Bound.Excluded(8)
            }
        val simplified = simplifyRange(range, 10)
        assertEquals(2, simplified.start)
        assertEquals(8, simplified.end)

        val unbounded =
            object : RangeBounds<Int> {
                override fun startBound(): Bound<Int> = Bound.Unbounded

                override fun endBound(): Bound<Int> = Bound.Unbounded
            }
        val simplifiedUnbounded = simplifyRange(unbounded, 10)
        assertEquals(0, simplifiedUnbounded.start)
        assertEquals(10, simplifiedUnbounded.end)
    }
}
