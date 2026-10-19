// port-lint: tests tests/named-threads.rs
package io.github.kotlinmania.rayon

import kotlin.test.Test
import kotlin.test.assertTrue

class NamedThreadsTest {
    @Test
    fun namedThreads() {
        ThreadPoolBuilder
            .new()
            .threadName { i -> "hello-name-test-$i" }
            .buildGlobal()
            .unwrap()

        val n = 100
        val names =
            (0 until n)
                .intoParIter()
                .map { "hello-name-test-0" }
                .toSet()

        val allContainsName = names.all { it.startsWith("hello-name-test-") }
        assertTrue(allContainsName)
    }
}
