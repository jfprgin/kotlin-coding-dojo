package exercise1

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class KotlinBasicsTest {

    // --- Task 1a: fullName ---

    @Test
    fun `fullName with all parts`() {
        assertEquals("John Michael Doe", fullName("John", "Michael", "Doe"))
    }

    @Test
    fun `fullName without middle name`() {
        assertEquals("John Doe", fullName("John", null, "Doe"))
    }

    @Test
    fun `fullName with blank first name returns null`() {
        assertNull(fullName("", null, "Doe"))
    }

    @Test
    fun `fullName with whitespace-only last name returns null`() {
        assertNull(fullName("John", "M", "   "))
    }

    @Test
    fun `fullName with blank first and present middle returns null`() {
        assertNull(fullName("  ", "Middle", "Doe"))
    }

    // --- Task 1b: groupAndCount ---

    @Test
    fun `groupAndCount basic`() {
        val result = groupAndCount(listOf("Apple", "avocado", "Banana", "blueberry"))
        assertEquals(mapOf('a' to 2, 'b' to 2), result)
    }

    @Test
    fun `groupAndCount ignores empty strings`() {
        val result = groupAndCount(listOf("", "cat", "", "Car"))
        assertEquals(mapOf('c' to 2), result)
    }

    @Test
    fun `groupAndCount empty list`() {
        assertEquals(emptyMap(), groupAndCount(emptyList()))
    }

    @Test
    fun `groupAndCount single element`() {
        assertEquals(mapOf('z' to 1), groupAndCount(listOf("Zebra")))
    }

    // --- Task 1c: repeatWithSeparator ---

    @Test
    fun `repeatWithSeparator normal case`() {
        assertEquals("ha-ha-ha", "ha".repeatWithSeparator(3, "-"))
    }

    @Test
    fun `repeatWithSeparator single repetition`() {
        assertEquals("x", "x".repeatWithSeparator(1, ","))
    }

    @Test
    fun `repeatWithSeparator zero repetitions`() {
        assertEquals("", "y".repeatWithSeparator(0, ","))
    }

    @Test
    fun `repeatWithSeparator negative repetitions`() {
        assertEquals("", "y".repeatWithSeparator(-1, ","))
    }

    @Test
    fun `repeatWithSeparator with multi-char separator`() {
        assertEquals("ab::ab", "ab".repeatWithSeparator(2, "::"))
    }

    // --- Task 1d: evaluate ---

    @Test
    fun `evaluate simple number`() {
        assertEquals(42.0, evaluate(Expr.Num(42.0)))
    }

    @Test
    fun `evaluate addition`() {
        assertEquals(5.0, evaluate(Expr.Add(Expr.Num(2.0), Expr.Num(3.0))))
    }

    @Test
    fun `evaluate multiplication`() {
        assertEquals(12.0, evaluate(Expr.Mul(Expr.Num(3.0), Expr.Num(4.0))))
    }

    @Test
    fun `evaluate division`() {
        assertEquals(2.5, evaluate(Expr.Div(Expr.Num(5.0), Expr.Num(2.0))))
    }

    @Test
    fun `evaluate division by zero returns null`() {
        assertNull(evaluate(Expr.Div(Expr.Num(5.0), Expr.Num(0.0))))
    }

    @Test
    fun `evaluate nested expression`() {
        // (2 + 3) * 4 = 20
        val expr = Expr.Mul(
            Expr.Add(Expr.Num(2.0), Expr.Num(3.0)),
            Expr.Num(4.0)
        )
        assertEquals(20.0, evaluate(expr))
    }

    @Test
    fun `evaluate nested with division by zero propagates null`() {
        // (10 / 0) + 5 = null (division by zero propagates)
        val expr = Expr.Add(
            Expr.Div(Expr.Num(10.0), Expr.Num(0.0)),
            Expr.Num(5.0)
        )
        assertNull(evaluate(expr))
    }
}
