package exercise3

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue

class TaskRunnerTest {

    // --- Task 3a: executionOrder ---

    @Test
    fun `simple linear dependency chain`() {
        val tasks = listOf(
            Task("c", listOf("b")),
            Task("b", listOf("a")),
            Task("a")
        )
        val order = executionOrder(tasks)
        assertEquals(listOf("a", "b", "c"), order)
    }

    @Test
    fun `no dependencies preserves order`() {
        val tasks = listOf(
            Task("alpha"),
            Task("beta"),
            Task("gamma")
        )
        val order = executionOrder(tasks)
        assertEquals(listOf("alpha", "beta", "gamma"), order)
    }

    @Test
    fun `diamond dependency`() {
        //     d
        //    / \
        //   b   c
        //    \ /
        //     a
        val tasks = listOf(
            Task("d", listOf("b", "c")),
            Task("c", listOf("a")),
            Task("b", listOf("a")),
            Task("a")
        )
        val order = executionOrder(tasks)
        assertEquals(4, order.size)
        assertTrue(order.indexOf("a") < order.indexOf("b"))
        assertTrue(order.indexOf("a") < order.indexOf("c"))
        assertTrue(order.indexOf("b") < order.indexOf("d"))
        assertTrue(order.indexOf("c") < order.indexOf("d"))
    }

    @Test
    fun `circular dependency throws`() {
        val tasks = listOf(
            Task("a", listOf("b")),
            Task("b", listOf("a"))
        )
        assertFailsWith<CircularDependencyException> {
            executionOrder(tasks)
        }
    }

    @Test
    fun `unknown dependency throws`() {
        val tasks = listOf(
            Task("a", listOf("missing"))
        )
        assertFailsWith<UnknownTaskException> {
            executionOrder(tasks)
        }
    }

    @Test
    fun `self dependency throws circular`() {
        val tasks = listOf(
            Task("a", listOf("a"))
        )
        assertFailsWith<CircularDependencyException> {
            executionOrder(tasks)
        }
    }

    // --- Task 3b: runTask ---

    @Test
    fun `runTask executes target and dependencies in order`() {
        val tasks = listOf(
            Task("compile", listOf("generate-sources")),
            Task("generate-sources"),
            Task("test", listOf("compile")),
            Task("package", listOf("test"))
        )
        val executed = mutableListOf<String>()
        runTask(tasks, "test") { executed.add(it) }

        assertEquals(listOf("generate-sources", "compile", "test"), executed)
    }

    @Test
    fun `runTask only executes needed tasks`() {
        val tasks = listOf(
            Task("a"),
            Task("b"),
            Task("c", listOf("a"))
        )
        val executed = mutableListOf<String>()
        runTask(tasks, "c") { executed.add(it) }

        assertEquals(listOf("a", "c"), executed)
        assertTrue("b" !in executed)
    }

    @Test
    fun `runTask with single task no deps`() {
        val tasks = listOf(Task("clean"))
        val executed = mutableListOf<String>()
        runTask(tasks, "clean") { executed.add(it) }

        assertEquals(listOf("clean"), executed)
    }

    @Test
    fun `runTask throws on unknown target`() {
        val tasks = listOf(Task("a"))
        assertFailsWith<UnknownTaskException> {
            runTask(tasks, "nonexistent") { }
        }
    }

    @Test
    fun `runTask throws on circular dependency`() {
        val tasks = listOf(
            Task("a", listOf("b")),
            Task("b", listOf("a"))
        )
        assertFailsWith<CircularDependencyException> {
            runTask(tasks, "a") { }
        }
    }

    @Test
    fun `runTask executes each task only once in diamond`() {
        val tasks = listOf(
            Task("base"),
            Task("left", listOf("base")),
            Task("right", listOf("base")),
            Task("top", listOf("left", "right"))
        )
        val executed = mutableListOf<String>()
        runTask(tasks, "top") { executed.add(it) }

        // base should appear exactly once
        assertEquals(1, executed.count { it == "base" })
        assertEquals(4, executed.size)
        assertTrue(executed.indexOf("base") < executed.indexOf("left"))
        assertTrue(executed.indexOf("base") < executed.indexOf("right"))
        assertTrue(executed.indexOf("left") < executed.indexOf("top"))
        assertTrue(executed.indexOf("right") < executed.indexOf("top"))
    }
}
