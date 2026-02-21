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
        return compareValuesBy(this, other,
            { it.major }, { it.minor}, { it.patch }
        )
    }

    override fun toString(): String = "$major.$minor.$patch"

    companion object {
        fun parse(version: String): SemVer? {
            val parts = version.split('.')

            if (parts.size != 3) return null

            return try {
                val major = parts[0].toInt()
                val minor = parts[1].toInt()
                val patch = parts[2].toInt()

                if (major < 0 || minor < 0 || patch < 0) return null

                SemVer(major, minor, patch)
            } catch (e: NumberFormatException) {
                null
            }
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
        val constraints = versionConstraint.split(',')

        return constraints.all { constraint ->
            val trimmed = constraint.trim()
            when {
                trimmed.startsWith(">=") -> {
                    val target = SemVer.parse(trimmed.substring(2))
                    target != null && version >= target
                }
                trimmed.startsWith(">") -> {
                    val target = SemVer.parse(trimmed.substring(1))
                    target != null && version > target
                }
                trimmed.startsWith("<=") -> {
                    val target = SemVer.parse(trimmed.substring(2))
                    target != null && version <= target
                }
                trimmed.startsWith("<") -> {
                    val target = SemVer.parse(trimmed.substring(1))
                    target != null && version < target
                }
                else -> {
                    val target = SemVer.parse(trimmed)
                    target != null && version == target
                }
            }
        }
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
    val groupedConstraints = dependencies.groupBy { it.name }

    return groupedConstraints.mapNotNull { (name, constraints) ->
        val versions = availableVersions[name] ?: return@mapNotNull null

        val bestVersion = versions
            .filter { version ->
                constraints.all { it.matches(version) }
            }
            .maxOrNull()

        if (bestVersion != null) name to bestVersion else null
    }.toMap()
}
