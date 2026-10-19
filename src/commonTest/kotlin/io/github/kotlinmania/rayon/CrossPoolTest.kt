// port-lint: tests tests/cross-pool.rs
package io.github.kotlinmania.rayon

import kotlin.test.Test
import kotlin.test.assertEquals

class CrossPoolTest {
    @Test
    fun crossPoolBusy() {
        val pool1 =
            ThreadPoolBuilder
                .new()
                .numThreads(1)
                .build()
                .unwrap()
        val pool2 =
            ThreadPoolBuilder
                .new()
                .numThreads(1)
                .build()
                .unwrap()

        val n = 100
        val sum =
            pool1.install {
                (1..n)
                    .intoParIter()
                    .map { i -> pool2.install { i } }
                    .sum()
            }
        assertEquals(n * (n + 1) / 2, sum)
    }
}
