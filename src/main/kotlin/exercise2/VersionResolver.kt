package exercise2

// =============================================================================
// Exercise 2: Version Resolver
//
// You are building a tool that resolves dependency versions (think Gradle/Make).
// Implement the classes and functions below step by step.
// All tests in exercise2/VersionResolverTest.kt must pass.
// =============================================================================

/**
 * Task 2a: Semantic Version
 *
 * Represent a semantic version (major.minor.patch) and implement:
 * - Parsing from string (e.g., "1.2.3") via companion object function [parse]
 * - Natural ordering: major first, then minor, then patch
 * - toString() returning "major.minor.patch"
 *
 * Invalid version strings should cause [parse] to return null.
 * Valid format: exactly 3 non-negative integers separated by dots.
 */
data class SemVer(val major: Int, val minor: Int, val patch: Int) : Comparable<SemVer> {

    override fun compareTo(other: SemVer): Int {
        TODO("Implement me")
    }

    override fun toString(): String {
        TODO("Implement me")
    }

    companion object {
        fun parse(version: String): SemVer? {
            TODO("Implement me")
        }
    }
}

/**
 * Task 2b: Dependency Declaration
 *
 * A dependency has a [name] and a [versionConstraint].
 *
 * Version constraints can be:
 * - Exact:   "1.2.3"         -> only this version
 * - Minimum: ">=1.2.0"       -> this version or higher
 * - Range:   ">=1.0.0,<2.0.0" -> between min (inclusive) and max (exclusive)
 *
 * Implement [matches] to check if a given SemVer satisfies the constraint.
 */
data class Dependency(val name: String, val versionConstraint: String) {

    fun matches(version: SemVer): Boolean {
        TODO("Implement me")
    }
}

/**
 * Task 2c: Resolve Dependencies
 *
 * Given:
 * - A list of [Dependency] declarations (possibly multiple per library with
 *   different constraints)
 * - A map of available versions per library name
 *
 * Return a map of library name -> highest version that satisfies ALL constraints
 * for that library.
 *
 * If no version satisfies all constraints for a library, the library should
 * not appear in the result map.
 */
fun resolveDependencies(
    dependencies: List<Dependency>,
    availableVersions: Map<String, List<SemVer>>
): Map<String, SemVer> {
    TODO("Implement me")
}
