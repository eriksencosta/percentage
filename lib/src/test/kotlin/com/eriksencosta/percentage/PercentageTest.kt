package com.eriksencosta.percentage

import org.junit.jupiter.api.DynamicTest.dynamicTest
import org.junit.jupiter.api.TestFactory
import org.junit.jupiter.api.assertThrows
import kotlin.test.Test
import kotlin.test.assertEquals

class PercentageTest {
    private data class AccessorsTestTable(
        val number: Number,
        val isZero: Boolean,
        val isNotZero: Boolean,
        val isPositive: Boolean,
        val isPositiveOrZero: Boolean,
        val isNegative: Boolean,
        val isNegativeOrZero: Boolean
    )

    private val zero = Percentage(0)
    private val one = Percentage(1)
    private val hundred = Percentage(100)
    private val minusOne = Percentage(-1)

    private val percentageCreation = listOf(
        Pair(100.0, 1.0),
        Pair(50.0, 0.5),
        Pair(25.0, 0.25),
        Pair(0.0, 0.0),
        Pair(0.25, 0.0025),
        Pair(0.50, 0.005),
        Pair(1.1, 0.011000000000000001),
        Pair(100.0 / 3, 0.33333333333333337),
    )

    private val percentageCreationWithPrecision = listOf(
        Triple(1.1, 1, 0.0),
        Triple(1.1, 2, 0.01),
        Triple(1.1, 3, 0.011),
        Triple(1.1, 4, 0.011),
        Triple(11.11, 1, 0.1),
        Triple(11.11, 2, 0.11),
        Triple(11.11, 3, 0.111),
        Triple(11.11, 4, 0.1111),
        Triple(100.0 / 3, 8, 0.33333333),
    )

    private val accessors = listOf(
        AccessorsTestTable(
            number = -1,
            isZero = false,
            isNotZero = true,
            isPositive = false,
            isPositiveOrZero = false,
            isNegative = true,
            isNegativeOrZero = true
        ),
        AccessorsTestTable(
            number = 0,
            isZero = true,
            isNotZero = false,
            isPositive = false,
            isPositiveOrZero = true,
            isNegative = false,
            isNegativeOrZero = true
        ),
        AccessorsTestTable(
            number = 1,
            isZero = false,
            isNotZero = true,
            isPositive = true,
            isPositiveOrZero = true,
            isNegative = false,
            isNegativeOrZero = false
        )
    )

    @TestFactory
    fun `Given a number When a Percentage is created Then a decimal value is calculated`() =
        percentageCreation
            .map { (number, expected) ->
                dynamicTest("when I pass $number then I should get $expected") {
                    assertEquals(expected, Percentage(number).decimal)
                }
            }

    @TestFactory
    fun `Given a number and a precision When a Percentage is created Then a precise decimal value is calculated`() =
        percentageCreationWithPrecision
            .map { (number, precision, expected) ->
                dynamicTest("when I pass $number and $precision then I should get $expected") {
                    assertEquals(expected, Percentage(number, precision).decimal)
                }
            }

    @TestFactory
    fun `Given a number When a Percentage is created Then isZero is calculated`() =
        accessors
            .map {
                dynamicTest("when I pass ${it.number} then I should get ${it.isZero}") {
                    assertEquals(it.isZero, Percentage(it.number).isZero)
                }
            }

    @TestFactory
    fun `Given a number When a Percentage is created Then isNotZero is calculated`() =
        accessors
            .map {
                dynamicTest("when I pass ${it.number} then I should get ${it.isNotZero}") {
                    assertEquals(it.isNotZero, Percentage(it.number).isNotZero)
                }
            }

    @TestFactory
    fun `Given a number When a Percentage is created Then isPositive is calculated`() =
        accessors
            .map {
                dynamicTest("when I pass ${it.number} then I should get ${it.isPositive}") {
                    assertEquals(it.isPositive, Percentage(it.number).isPositive)
                }
            }

    @TestFactory
    fun `Given a number When a Percentage is created Then isPositiveOrZero is calculated`() =
        accessors
            .map {
                dynamicTest("when I pass ${it.number} then I should get ${it.isPositiveOrZero}") {
                    assertEquals(it.isPositiveOrZero, Percentage(it.number).isPositiveOrZero)
                }
            }

    @TestFactory
    fun `Given a number When a Percentage is created Then isNegative is calculated`() =
        accessors
            .map {
                dynamicTest("when I pass ${it.number} then I should get ${it.isNegative}") {
                    assertEquals(it.isNegative, Percentage(it.number).isNegative)
                }
            }

    @TestFactory
    fun `Given a number When a Percentage is created Then isNegativeOrZero is calculated`() =
        accessors
            .map {
                dynamicTest("when I pass ${it.number} then I should get ${it.isNegativeOrZero}") {
                    assertEquals(it.isNegativeOrZero, Percentage(it.number).isNegativeOrZero)
                }
            }

    @TestFactory
    fun `Given two numbers When ratioOf is called Then a Percentage of them is returned`() =
        Fixtures.ratioOf
            .map { (number, other, expected) ->
                dynamicTest("when I pass $number and $other then I should get $expected") {
                    assertEquals(expected, Percentage.ratioOf(number, other))
                }
            }

    @TestFactory
    fun `Given two numbers and a precision When ratioOf is called Then a precise Percentage of them is returned`() =
        Fixtures.ratioOfWithPrecision
            .map { (number, other, precision, expected) ->
                dynamicTest("when I pass $number, $other, and $precision then I should get $expected") {
                    assertEquals(expected, Percentage.ratioOf(number, other, precision))
                }
            }

    @Test
    fun `Given two numbers with the second being zero When of is called Then an Exception is thrown`() {
        val exception = assertThrows<ArgumentCanNotBeZero> { Percentage.ratioOf(1, 0) }
        assertEquals("The argument \"other\" can not be zero", exception.message)
    }

    @TestFactory
    fun `Given two numbers When relativeChange is called Then a Percentage change of them is returned`() =
        Fixtures.changeOf
            .map { (initial, ending, expected) ->
                dynamicTest("when I pass $initial and $ending then I should get $expected") {
                    assertEquals(expected, Percentage.relativeChange(initial, ending))
                }
            }

    @TestFactory
    fun `Given two numbers and a precision When relativeChange is called Then a precise Percentage change of them is returned`() =
        Fixtures.changeOfWithPrecision
            .map { (initial, ending, precision, expected) ->
                dynamicTest("when I pass $initial, $ending, and $precision then I should get $expected") {
                    assertEquals(expected, Percentage.relativeChange(initial, ending, precision))
                }
            }

    @Test
    fun `Given two numbers with the first being zero When relativeChange is called Then an Exception is thrown`() {
        val exception = assertThrows<ArgumentCanNotBeZero> { Percentage.relativeChange(0, 100) }
        assertEquals("The argument \"initial\" can not be zero", exception.message)
    }

    @TestFactory
    fun `Given a Percentage and a precision When with is called Then a precise Percentage is returned`() =
        listOf(
            Triple(Percentage(100), 2, Percentage(100, 2)),
            Triple(Percentage(100), 4, Percentage(100, 4)),
            Triple(Percentage(100, 4), 6, Percentage(100, 6)),
            Triple(Percentage(100, 6), 6, Percentage(100, 6)),
        )
            .map { (percentage, precision, expected) ->
                dynamicTest("given $percentage when I pass $precision then I should get $expected") {
                    assertEquals(expected, percentage.with(precision))
                }
            }

    @TestFactory
    fun `Given a Percentage and a number When valueWhen is called Then a proportional number is returned`() =
        Fixtures.numberOf
            .map { (percentage, number, expected) ->
                dynamicTest("given $percentage when I pass $number then I should get $expected") {
                    assertEquals(expected, percentage.valueWhen(number))
                }
            }

    @Test
    fun `Given a Percentage for zero and a number When valueWhen is called Then an Exception is thrown`() {
        val exception = assertThrows<OperationUndefinedForZero> { Percentage(0).valueWhen(5) }
        assertEquals("Can not calculate the number when Percentage is zero", exception.message)
    }

    @TestFactory
    fun `Given a Percentage When unaryPlus is called Then a positive Percentage is returned`() =
        listOf(
            Pair(minusOne, one),
            Pair(zero, zero),
            Pair(one, one),

            // Precision case
            Pair(Percentage(-11.11, 4), Percentage(11.11, 4))
        )
            .map { (percentage, expected) ->
                dynamicTest("given $percentage when I switch its sign to positive then I should get $expected") {
                    assertEquals(expected, +percentage)
                }
            }

    @TestFactory
    fun `Given a Percentage When unaryMinus is called Then a negated Percentage is returned`() =
        listOf(
            Pair(one, minusOne),
            Pair(zero, zero),
            Pair(minusOne, one),

            // Precision case
            Pair(Percentage(11.11, 4), Percentage(-11.11, 4))
        )
            .map { (percentage, expected) ->
                dynamicTest("given $percentage when I negate it then I should get $expected") {
                    assertEquals(expected, -percentage)
                }
            }

    @TestFactory
    fun `Given a Percentage and a number When times is called Then a number representing the Percentage is returned`() =
        Fixtures.times
            .map { (number, percentage, expected) ->
                dynamicTest("given $percentage when I multiply by $number then I should get $expected") {
                    assertEquals(expected, percentage * number)
                }
            }

    @TestFactory
    fun `Given a Percentage and a number When plus is called Then a number increased by the Percentage is returned`() =
        Fixtures.plus
            .map { (number, percentage, expected) ->
                dynamicTest("given $percentage when I increase $number with it then I should get $expected") {
                    assertEquals(expected, percentage + number)
                }
            }

    @TestFactory
    fun `Given a Percentage and a number When minus is called Then a number decreased by the Percentage is returned`() =
        Fixtures.subtraction
            .map { (number, percentage, expected) ->
                dynamicTest("given $percentage when I decrease $number with it then I should get $expected") {
                    assertEquals(expected, percentage - number)
                }
            }

    @TestFactory
    fun `Given two Percentages When equals is called Then a Boolean is returned`() =
        listOf(
            Triple(one, one, true),
            Triple(one, null, false),
            Triple(hundred, Percentage(100), true),
            Triple(Percentage(100), Percentage(100), true),
            Triple(Percentage(100), Percentage(50), false),
            Triple(Percentage(12.5), 12.5 / 100, false),
        )
            .map { (percentage, other, expected) ->
                dynamicTest("given $percentage when I compare against $other then I should get $expected") {
                    assertEquals(expected, percentage == other)
                }
            }

    @TestFactory
    fun `Given two precise Percentages When equals is called Then a Boolean is returned`() =
        listOf(
            Triple(Percentage(100, 2), Percentage(100.00, 2), true),
            Triple(Percentage(100, 2), Percentage(99.99, 2), false),
            Triple(Percentage(100, 2), Percentage(100), false),
        )
            .map { (percentage, other, expected) ->
                dynamicTest("given $percentage when I compare against $other then I should get $expected") {
                    assertEquals(expected, percentage == other)
                }
            }

    @TestFactory
    fun `Given two Percentages When compareTo is called Then a comparison value is returned`() =
        listOf(
            Triple(Percentage(100), Percentage(200), -1),
            Triple(Percentage(100), Percentage(100), 0),
            Triple(Percentage(100), Percentage(50), 1),
        )
            .map { (percentage, other, expected) ->
                dynamicTest("given $percentage when I compare against $other then I should get $expected") {
                    assertEquals(expected, percentage.compareTo(other))
                }
            }

    @Test
    fun `Given a Percentage When hashCode is called Then a hash value is returned`() =
        assertEquals(1072693248, Percentage(100).hashCode())

    @TestFactory
    fun `Given a Percentage When toString is called Then an appropriately formatted string is returned`() =
        listOf(
            Pair(Percentage(100, -4), "100%"),
            Pair(Percentage(100), "100%"),
            Pair(Percentage(100, 0), "100%"),
            Pair(Percentage(100, 2), "100.00%"),
            Pair(Percentage(100, 4), "100.0000%"),
        )
            .map { (percentage, expected) ->
                dynamicTest("given $percentage when I convert it to string then I should get $expected") {
                    assertEquals(expected, percentage.toString())
                }
            }
}