package exercise2

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue
import kotlin.test.assertFalse

class VersionResolverTest {

    // --- Task 2a: SemVer ---

    @Test
    fun `parse valid version`() {
        val v = SemVer.parse("1.2.3")
        assertEquals(SemVer(1, 2, 3), v)
    }

    @Test
    fun `parse version with zeros`() {
        assertEquals(SemVer(0, 0, 1), SemVer.parse("0.0.1"))
    }

    @Test
    fun `parse invalid version returns null`() {
        assertNull(SemVer.parse("1.2"))
        assertNull(SemVer.parse("abc"))
        assertNull(SemVer.parse("1.2.3.4"))
        assertNull(SemVer.parse(""))
        assertNull(SemVer.parse("1.-1.0"))
    }

    @Test
    fun `semver toString`() {
        assertEquals("1.2.3", SemVer(1, 2, 3).toString())
    }

    @Test
    fun `semver comparison`() {
        assertTrue(SemVer(1, 0, 0) < SemVer(2, 0, 0))
        assertTrue(SemVer(1, 1, 0) < SemVer(1, 2, 0))
        assertTrue(SemVer(1, 1, 1) < SemVer(1, 1, 2))
        assertTrue(SemVer(1, 0, 0) == SemVer(1, 0, 0))
        assertTrue(SemVer(2, 0, 0) > SemVer(1, 9, 9))
    }

    @Test
    fun `semver sorting`() {
        val versions = listOf(
            SemVer(1, 10, 0),
            SemVer(1, 2, 0),
            SemVer(2, 0, 0),
            SemVer(1, 2, 1)
        )
        val sorted = versions.sorted()
        assertEquals(
            listOf(SemVer(1, 2, 0), SemVer(1, 2, 1), SemVer(1, 10, 0), SemVer(2, 0, 0)),
            sorted
        )
    }

    // --- Task 2b: Dependency matching ---

    @Test
    fun `exact version match`() {
        val dep = Dependency("lib", "1.2.3")
        assertTrue(dep.matches(SemVer(1, 2, 3)))
        assertFalse(dep.matches(SemVer(1, 2, 4)))
    }

    @Test
    fun `minimum version constraint`() {
        val dep = Dependency("lib", ">=1.2.0")
        assertTrue(dep.matches(SemVer(1, 2, 0)))
        assertTrue(dep.matches(SemVer(1, 3, 0)))
        assertTrue(dep.matches(SemVer(2, 0, 0)))
        assertFalse(dep.matches(SemVer(1, 1, 9)))
    }

    @Test
    fun `range version constraint`() {
        val dep = Dependency("lib", ">=1.0.0,<2.0.0")
        assertTrue(dep.matches(SemVer(1, 0, 0)))
        assertTrue(dep.matches(SemVer(1, 9, 9)))
        assertFalse(dep.matches(SemVer(2, 0, 0)))
        assertFalse(dep.matches(SemVer(0, 9, 0)))
    }

    // --- Task 2c: resolveDependencies ---

    @Test
    fun `resolve single dependency`() {
        val deps = listOf(Dependency("kotlin-stdlib", ">=1.9.0"))
        val available = mapOf(
            "kotlin-stdlib" to listOf(
                SemVer(1, 8, 0),
                SemVer(1, 9, 0),
                SemVer(1, 9, 22),
                SemVer(2, 0, 0)
            )
        )
        val result = resolveDependencies(deps, available)
        assertEquals(SemVer(2, 0, 0), result["kotlin-stdlib"])
    }

    @Test
    fun `resolve with conflicting constraints picks highest satisfying all`() {
        val deps = listOf(
            Dependency("guava", ">=30.0.0"),
            Dependency("guava", ">=30.0.0,<32.0.0")
        )
        val available = mapOf(
            "guava" to listOf(
                SemVer(29, 0, 0),
                SemVer(30, 1, 0),
                SemVer(31, 0, 0),
                SemVer(32, 0, 0)
            )
        )
        val result = resolveDependencies(deps, available)
        assertEquals(SemVer(31, 0, 0), result["guava"])
    }

    @Test
    fun `resolve with no satisfying version`() {
        val deps = listOf(
            Dependency("lib", ">=2.0.0"),
            Dependency("lib", ">=1.0.0,<2.0.0")
        )
        val available = mapOf(
            "lib" to listOf(SemVer(1, 0, 0), SemVer(2, 0, 0))
        )
        val result = resolveDependencies(deps, available)
        assertFalse(result.containsKey("lib"))
    }

    @Test
    fun `resolve multiple libraries`() {
        val deps = listOf(
            Dependency("a", ">=1.0.0"),
            Dependency("b", "2.0.0")
        )
        val available = mapOf(
            "a" to listOf(SemVer(1, 0, 0), SemVer(1, 1, 0)),
            "b" to listOf(SemVer(1, 0, 0), SemVer(2, 0, 0))
        )
        val result = resolveDependencies(deps, available)
        assertEquals(SemVer(1, 1, 0), result["a"])
        assertEquals(SemVer(2, 0, 0), result["b"])
    }
}
