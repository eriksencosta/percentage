package com.eriksencosta.math.percentage

import com.eriksencosta.math.common.Rounding
import org.junit.jupiter.api.DynamicTest.dynamicTest
import org.junit.jupiter.api.TestFactory
import org.junit.jupiter.api.assertThrows
import java.math.RoundingMode
import kotlin.test.Test
import kotlin.test.assertEquals

class PercentageTest {
    private val zero = Percentage.of(0)
    private val one = Percentage.of(1)
    private val hundred = Percentage.of(100)
    private val minusOne = Percentage.of(-1)
    private val oneThird = (1 / 3.0) * 100

    @TestFactory
    fun `Create a Percentage`() = listOf(
        Pair(50, Percentage.of(50)),
        Pair(100, Percentage.of(100)),
    )
        .map { (number, expected) ->
            dynamicTest("given $number when I create a Percentage then I should get $expected") {
                assertEquals(expected, number.percent())
                assertEquals(expected, number.toPercentage())
            }
        }

    @TestFactory
    fun `Create a Percentage with PreciseRounding when given a precision`() = listOf(
        Triple(50, 2, Percentage.of(50, 2)),
        Triple(50, 4, Percentage.of(50, 4)),
    )
        .map { (number, precision, expected) ->
            dynamicTest("given $number and $precision when I create a Percentage then I should get $expected") {
                assertEquals(expected, number.percent(precision))
                assertEquals(expected, number.toPercentage(precision))
            }
        }

    @TestFactory
    fun `Create a Percentage with a Rounding strategy`() = listOf(
        Triple(50, Rounding.no(), Percentage.of(50, Rounding.no())),
        Triple(50, Rounding.to(2), Percentage.of(50, Rounding.to(2))),
        Triple(50, Rounding.to(4, RoundingMode.HALF_DOWN), Percentage.of(50, Rounding.to(4, RoundingMode.HALF_DOWN))),
    )
        .map { (number, rounding, expected) ->
            dynamicTest("given $number and $rounding when I create a Percentage then I should get $expected") {
                assertEquals(expected, number.percent(rounding))
                assertEquals(expected, number.toPercentage(rounding))
            }
        }

    @TestFactory
    fun `Return true when the percentage is zero`() = Fixtures.accessors
        .map {
            dynamicTest("given percentage for ${it.number} then I should get ${it.isZero}") {
                assertEquals(it.isZero, Percentage.of(it.number).isZero)
            }
        }

    @TestFactory
    fun `Return true when the percentage is not zero`() = Fixtures.accessors
        .map {
            dynamicTest("given percentage for ${it.number} then I should get ${it.isNotZero}") {
                assertEquals(it.isNotZero, Percentage.of(it.number).isNotZero)
            }
        }

    @TestFactory
    fun `Return true when the percentage is positive`() = Fixtures.accessors
        .map {
            dynamicTest("given percentage for ${it.number} then I should get ${it.isPositive}") {
                assertEquals(it.isPositive, Percentage.of(it.number).isPositive)
            }
        }

    @TestFactory
    fun `Return true when the percentage is positive or zero`() = Fixtures.accessors
        .map {
            dynamicTest("given percentage for ${it.number} then I should get ${it.isPositiveOrZero}") {
                assertEquals(it.isPositiveOrZero, Percentage.of(it.number).isPositiveOrZero)
            }
        }

    @TestFactory
    fun `Return true when the percentage is negative`() = Fixtures.accessors
        .map {
            dynamicTest("given percentage for ${it.number} then I should get ${it.isNegative}") {
                assertEquals(it.isNegative, Percentage.of(it.number).isNegative)
            }
        }

    @TestFactory
    fun `Return true when the percentage is negative or zero`() = Fixtures.accessors
        .map {
            dynamicTest("given percentage for ${it.number} then I should get ${it.isNegativeOrZero}") {
                assertEquals(it.isNegativeOrZero, Percentage.of(it.number).isNegativeOrZero)
            }
        }

    @TestFactory
    fun `Calculate the percentage ratio of two numbers`() = Fixtures.ratioOf
        .map { (number, other, expected) ->
            dynamicTest("given numbers $number and $other then I should get $expected") {
                assertEquals(expected, Percentage.ratioOf(number, other))
                assertEquals(expected, number ratioOf other)
            }
        }

    @TestFactory
    fun `Calculate the precise percentage ratio of two numbers`() = Fixtures.ratioOfWithPrecision
        .map { (number, other, precision, expected) ->
            dynamicTest("given numbers $number and $other, and precision $precision then I should get $expected") {
                assertEquals(expected, Percentage.ratioOf(number, other, precision))
                assertEquals(expected, number.ratioOf(other, precision))
            }
        }

    @TestFactory
    fun `Calculate the rounded percentage ratio of two numbers`() = Fixtures.ratioOfWithRounding
        .map { (number, other, rounding, expected) ->
            dynamicTest("given numbers $number and $other, and rounding $rounding then I should get $expected") {
                assertEquals(expected, Percentage.ratioOf(number, other, rounding))
                assertEquals(expected, number.ratioOf(other, rounding))
            }
        }

    @Test
    fun `Throw exception when calculating the percentage ratio of a number and zero`() =
        assertThrows<IllegalArgumentException> { Percentage.ratioOf(1, 0) }.run {
            assertEquals("The argument \"other\" can not be zero", message)
        }

    @TestFactory
    fun `Calculate the relative change percentage of two numbers`() = Fixtures.relativeChange
        .map { (initial, ending, expected) ->
            dynamicTest("given $initial and $ending then I should get $expected") {
                assertEquals(expected, Percentage.relativeChange(initial, ending))
                assertEquals(expected, initial relativeChange ending)
            }
        }

    @TestFactory
    fun `Calculate the precise relative change percentage of two numbers`() = Fixtures.relativeChangeWithPrecision
        .map { (initial, ending, precision, expected) ->
            dynamicTest("given numbers $initial and $ending, and precision $precision then I should get $expected") {
                assertEquals(expected, Percentage.relativeChange(initial, ending, precision))
                assertEquals(expected, initial.relativeChange(ending, precision))
            }
        }

    @TestFactory
    fun `Calculate the rounded relative change percentage of two numbers`() = Fixtures.roundedChangeWithPrecision
        .map { (initial, ending, rounding, expected) ->
            dynamicTest("given numbers $initial and $ending, and rounding $rounding then I should get $expected") {
                assertEquals(expected, Percentage.relativeChange(initial, ending, rounding))
                assertEquals(expected, initial.relativeChange(ending, rounding))
            }
        }

    @Test
    fun `Throw exception when calculating the relative change for an interval starting with zero`() =
        assertThrows<IllegalArgumentException> { Percentage.relativeChange(0, 100) }.run {
            assertEquals("The argument \"initial\" can not be zero", message)
        }

    @TestFactory
    fun `Apply a precision to a percentage returns a precise percentage`() = listOf(
        Triple(Percentage.of(100), 2, Percentage.of(100, 2)),
        Triple(Percentage.of(100), 4, Percentage.of(100, 4)),
        Triple(Percentage.of(100, 4), 4, Percentage.of(100, 4)),
        Triple(Percentage.of(100, 4), 6, Percentage.of(100, 6)),
        Triple(Percentage.of(100, 6), 6, Percentage.of(100, 6)),
        Triple(
            Percentage.of(100, Rounding.to(6, RoundingMode.HALF_DOWN)),
            6,
            Percentage.of(100, Rounding.to(6, RoundingMode.HALF_DOWN))
        ),
    )
        .map { (percentage, precision, expected) ->
            dynamicTest("given $percentage when I apply precision $precision then I should get $expected") {
                assertEquals(expected, percentage with precision)
            }
        }

    @TestFactory
    fun `Apply a rounding to a percentage returns a rounded percentage`() = listOf(
        Triple(
            Percentage.of(100),
            Rounding.no(),
            Percentage.of(100)
        ),
        Triple(
            Percentage.of(100),
            Rounding.to(2, RoundingMode.HALF_DOWN),
            Percentage.of(100, Rounding.to(2, RoundingMode.HALF_DOWN))
        ),
        Triple(
            Percentage.of(100, Rounding.to(2, RoundingMode.UP)),
            Rounding.no(),
            Percentage.of(100, Rounding.no())
        ),
    )
        .map { (percentage, rounding, expected) ->
            dynamicTest("given $percentage when I apply rounding $rounding then I should get $expected") {
                assertEquals(expected, percentage with rounding)
            }
        }

    @TestFactory
    fun `Calculate the base value of a percentage and its numeric value`() = Fixtures.valueWhen
        .map { (percentage, number, expected) ->
            dynamicTest("given $percentage when I calculate the value for $number then I should get $expected") {
                assertEquals(expected, percentage valueWhen number)
                assertEquals(expected, number valueWhen percentage)
            }
        }

    @Test
    fun `Throw exception when calculating the base value for zero percent`() =
        assertThrows<IllegalStateException> { Percentage.of(0) valueWhen 5 }.run {
            assertEquals("This operation can not execute when Percentage is zero", message)
        }

    @TestFactory
    fun `Cast the percentage to its positive value`() = listOf(
        Pair(minusOne, one),
        Pair(zero, zero),
        Pair(one, one),

        // Precision case
        Pair(Percentage.of(-11.11, 4), Percentage.of(11.11, 4)),

        // Rounding case
        Pair(
            Percentage.of(-11.11, Rounding.to(2, RoundingMode.HALF_DOWN)),
            Percentage.of(11.11, Rounding.to(2, RoundingMode.HALF_DOWN))
        ),
    )
        .map { (percentage, expected) ->
            dynamicTest("given $percentage when I cast it to positive then I should get $expected") {
                assertEquals(expected, +percentage)
            }
        }

    @TestFactory
    fun `Negate the percentage`() = listOf(
        Pair(one, minusOne),
        Pair(zero, zero),
        Pair(minusOne, one),

        // Precision case
        Pair(Percentage.of(11.11, 4), Percentage.of(-11.11, 4)),

        // Rounding case
        Pair(
            Percentage.of(11.11, Rounding.to(2, RoundingMode.HALF_DOWN)),
            Percentage.of(-11.11, Rounding.to(2, RoundingMode.HALF_DOWN))
        ),
    )
        .map { (percentage, expected) ->
            dynamicTest("given $percentage when I negate it then I should get $expected") {
                assertEquals(expected, -percentage)
            }
        }

    @TestFactory
    fun `Multiply a percentage by a number`() = Fixtures.times
        .map { (number, percentage, expected) ->
            dynamicTest("given $percentage when I multiply by $number then I should get $expected") {
                assertEquals(expected, percentage * number)
                assertEquals(expected, number * percentage)
            }
        }

    @TestFactory
    fun `Increase a number by a percentage`() = Fixtures.increase
        .map { (number, percentage, expected) ->
            dynamicTest("given $percentage when I increase $number with it then I should get $expected") {
                assertEquals(expected, percentage increase number)
                assertEquals(expected, number increaseBy percentage)
            }
        }

    @TestFactory
    fun `Decrease a number by a percentage`() = Fixtures.decrease
        .map { (number, percentage, expected) ->
            dynamicTest("given $percentage when I decrease $number with it then I should get $expected") {
                assertEquals(expected, percentage decrease number)
                assertEquals(expected, number decreaseBy percentage)
            }
        }

    @TestFactory
    fun `Compare two percentages for equality`() = listOf(
        Triple(one, one, true),
        Triple(one, null, false),
        Triple(hundred, Percentage.of(100), true),
        Triple(Percentage.of(100), Percentage.of(100), true),
        Triple(Percentage.of(100), Percentage.of(50), false),
        Triple(Percentage.of(12.5), 12.5 / 100, false),
    )
        .map { (percentage, other, expected) ->
            dynamicTest("given $percentage when I compare against $other then I should get $expected") {
                assertEquals(expected, percentage == other)
            }
        }

    @TestFactory
    fun `Compare two precise percentages for equality`() = listOf(
        Triple(Percentage.of(100, 2), Percentage.of(100.00, 2), true),
        Triple(Percentage.of(100, 2), Percentage.of(99.99, 2), false),
        Triple(Percentage.of(100, 2), Percentage.of(100), false),
        Triple(
            Percentage.of(100, Rounding.to(2, RoundingMode.HALF_DOWN)),
            Percentage.of(100, Rounding.to(2)),
            false
        ),
    )
        .map { (percentage, other, expected) ->
            dynamicTest("given $percentage when I compare against $other then I should get $expected") {
                assertEquals(expected, percentage == other)
            }
        }

    @TestFactory
    fun `Compare two percentages for order`() = listOf(
        Triple(Percentage.of(100), Percentage.of(200), -1),
        Triple(Percentage.of(100), Percentage.of(100), 0),
        Triple(Percentage.of(100), Percentage.of(50), 1),

        Triple(Percentage.of(100, 1), Percentage.of(100, 2), -1),
        Triple(Percentage.of(100, 2), Percentage.of(100, 1), 1),
        Triple(Percentage.of(100, 2), Percentage.of(100, 2), 0),
        Triple(Percentage.of(100, 2), Percentage.of(100), -1),

        Triple(Percentage.of(oneThird, 1), Percentage.of(oneThird, 2), -1),
        Triple(Percentage.of(oneThird, 2), Percentage.of(oneThird, 1), 1),
        Triple(Percentage.of(oneThird, 2), Percentage.of(oneThird, 2), 0),

        // unboundless percentages are always greater than precise percentages
        Triple(Percentage.of(oneThird, 3), Percentage.of(oneThird), -1),
        Triple(Percentage.of(oneThird), Percentage.of(oneThird, 3), 1),
    )
        .map { (percentage, other, expected) ->
            dynamicTest("given $percentage when I compare against $other then I should get $expected") {
                assertEquals(expected, percentage.compareTo(other))
            }
        }

    @Test
    fun `Order a collection of percentages`() {
        val expected = listOf(
            Percentage.of(10),
            Percentage.of(20),
            Percentage.of(30),
            Percentage.of(oneThird, 1),
            Percentage.of(oneThird, 2),
            Percentage.of(oneThird, 3),
            Percentage.of(oneThird),
            Percentage.of(40),
            Percentage.of(50, 2),
            Percentage.of(50),
        )

        val percentages = listOf(
            Percentage.of(50, 2),
            Percentage.of(20),
            Percentage.of(50),
            Percentage.of(oneThird, 3),
            Percentage.of(40),
            Percentage.of(30),
            Percentage.of(10),
            Percentage.of(oneThird, 2),
            Percentage.of(oneThird),
            Percentage.of(oneThird, 1),
        )

        assertEquals(expected, percentages.sorted())
    }

    @TestFactory
    fun `Calculate the percentage hash code`() = listOf(
        Percentage.of(100) to 1_041_237_863,
        Percentage.of(100, 2) to -1_106_245_692,
    )
        .map { (percentage, expected) ->
            dynamicTest("given $percentage when I calculate its hash code then I should get $expected") {
                assertEquals(expected, percentage.hashCode())
            }
        }

    @TestFactory
    fun `Convert the percentage to string`() = listOf(
        Pair(Percentage.of(-10), "-10%"),
        Pair(Percentage.of(1), "1%"),
        Pair(Percentage.of(100), "100%"),
        Pair(Percentage.of(oneThird), "33.33%"),
        Pair(Percentage.of(-100.0 / 3.0), "-33.33%"),
        Pair(Percentage.of(-100.0 / 3.0, 2), "-33.33%"),
        Pair(Percentage.of(513.0 / 53.0), "9.68%"),
    )
        .map { (percentage, expected) ->
            dynamicTest("given $percentage when I convert it to string then I should get $expected") {
                assertEquals(expected, percentage.toString())
            }
        }
}
