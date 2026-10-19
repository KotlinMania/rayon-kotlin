package io.github.kotlinmania.rayon

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class ThreadPoolTest {
    @Test
    fun testThreadPoolScopeRunsOnce() {
        var starts = 0
        var operations = 0
        var spawned = 0
        val pool =
            ThreadPoolBuilder
                .new()
                .numThreads(1)
                .startHandler { index ->
                    assertEquals(0, index)
                    starts++
                    assertEquals(1, starts)
                }.build()
                .unwrap()

        val result =
            pool.scope { scope ->
                operations++
                scope.spawn { spawned++ }
                42
            }

        assertEquals(42, result)
        assertEquals(1, starts)
        assertEquals(1, operations)
        assertEquals(1, spawned)
    }

    @Test
    fun testThreadPoolBuild() {
        val pool =
            ThreadPoolBuilder
                .new()
                .numThreads(4)
                .stackSize(1024uL)
                .build()
                .unwrap()

        assertEquals(4, pool.currentNumThreads())

        val result = pool.install { 42 }
        assertEquals(42, result)

        val pair = pool.join({ 1 }, { 2 })
        assertEquals(Pair(1, 2), pair)

        val scopedResult =
            pool.scope { s ->
                var executed = false
                s.spawn { executed = true }
                executed
            }
        assertTrue(scopedResult)
    }

    @Test
    fun testJoinAndScope() {
        val (a, b) = join({ "left" }, { "right" })
        assertEquals("left", a)
        assertEquals("right", b)

        assertEquals(1, currentNumThreads())
        assertEquals(0, currentThreadIndex())
        assertEquals(64, maxNumThreads())

        val res =
            scope { s ->
                var count = 0
                s.spawn { count++ }
                count
            }
        assertEquals(1, res)

        val inPlaceRes =
            inPlaceScope { s ->
                var count = 10
                s.spawn { count += 5 }
                count
            }
        assertEquals(15, inPlaceRes)
    }

    @Test
    fun testPreludeChunking() {
        val list = listOf(1, 2, 3, 4, 5)
        val chunks = list.parChunks(2)
        assertEquals(listOf(listOf(1, 2), listOf(3, 4), listOf(5)), chunks)

        val exactChunks = list.parChunksExact(2)
        assertEquals(listOf(listOf(1, 2), listOf(3, 4)), exactChunks)

        val parIterResult = list.parIter().map { it * 2 }.toList()
        assertEquals(listOf(2, 4, 6, 8, 10), parIterResult)
    }
}
