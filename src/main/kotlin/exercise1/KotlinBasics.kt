package exercise1

// =============================================================================
// Exercise 1: Kotlin Fundamentals
//
// Implement the functions below. All tests in exercise1/KotlinBasicsTest.kt
// must pass. You may add helper functions or classes as needed.
// =============================================================================

/**
 * Task 1a: Null-safe full name
 *
 * Given a first name, optional middle name, and last name,
 * return the full name as a single string.
 * - If middleName is null, omit it (no double spaces).
 * - If firstName or lastName is blank (empty or whitespace only), return null.
 */
fun fullName(firstName: String, middleName: String?, lastName: String): String? {
    if (firstName.isBlank() || lastName.isBlank()) {
        return null
    }
    if (middleName == null) {
        return "$firstName $lastName"
    } else {
        return "$firstName $middleName $lastName"
    }
}

/**
 * Task 1b: Group and count
 *
 * Given a list of strings, return a map where:
 * - Key = first character (lowercase) of the string
 * - Value = number of strings starting with that character (case-insensitive)
 *
 * Empty strings should be ignored
 */
fun groupAndCount(items: List<String>): Map<Char, Int> {
    return items.filter { it.isNotEmpty() }
        .groupingBy { it.first().lowercaseChar() }
        .eachCount()
}

/**
 * Task 1c: Extension function - repeat with separator
 *
 * Create an extension function on String that repeats the string [n] times,
 * joined by [separator].
 *
 * If n <= 0, return an empty string.
 * If n == 1, return the original string (no separator).
 */
fun String.repeatWithSeparator(n: Int, separator: String): String {
    if (n <= 0) return ""

    if (n == 1) return this

    return (1..n).joinToString(separator) { this }
}

/**
 * Task 1d: Sealed class - Evaluate expressions
 *
 * Given the sealed class hierarchy below, implement the [evaluate] function
 * that computes the numeric result of an expression.
 *
 * Division by zero should return null.
 */
sealed class Expr {
    data class Num(val value: Double) : Expr()
    data class Add(val left: Expr, val right: Expr) : Expr()
    data class Mul(val left: Expr, val right: Expr) : Expr()
    data class Div(val left: Expr, val right: Expr) : Expr()
}

fun evaluate(expr: Expr): Double? {

    return when (expr) {
        is Expr.Num -> expr.value
        is Expr.Add -> {
            val left = evaluate(expr.left)?: return null
            val right = evaluate(expr.right)?: return null
            left + right
        }
        is Expr.Div -> {
            val left = evaluate(expr.left)?: return null
            val right = (evaluate(expr.right))?: return null
            if (right == 0.0) {
                return null
            }
            left / right
        }
        is Expr.Mul -> {
            val left = evaluate(expr.left)?: return null
            val right = evaluate(expr.right)?: return null
            left * right
        }
    }
}
