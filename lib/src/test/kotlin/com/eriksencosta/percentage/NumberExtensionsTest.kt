package com.eriksencosta.percentage

import org.junit.jupiter.api.DynamicTest.dynamicTest
import org.junit.jupiter.api.TestFactory
import kotlin.test.assertEquals

class NumberExtensionsTest {
    private val percentageCreation = listOf(
        Pair(100.0, Percentage(100)),
        Pair(50.0, Percentage(50)),
        Pair(25.0, Percentage(25)),
        Pair(0.0, Percentage(0)),
        Pair(0.25, Percentage(0.25)),
        Pair(0.5, Percentage(0.5)),
        Pair(1.1, Percentage(1.1)),
        Pair(100.0 / 3, Percentage(100.0 / 3)),
    )

    private val percentageCreationWithPrecision = listOf(
        Triple(1.1, 1, Percentage(1.1, 1)),
        Triple(1.1, 2, Percentage(1.1, 2)),
        Triple(1.1, 3, Percentage(1.1, 3)),
        Triple(1.1, 4, Percentage(1.1, 4)),
        Triple(11.11, 1, Percentage(11.11, 1)),
        Triple(11.11, 2, Percentage(11.11, 2)),
        Triple(11.11, 3, Percentage(11.11, 3)),
        Triple(11.11, 4, Percentage(11.11, 4)),
        Triple(100.0 / 3, 8, Percentage(100.0 / 3, 8)),
    )

    @TestFactory
    fun `Given a Number When percent is called Then a Percentage is returned`() =
        percentageCreation
            .map { (number, expected) ->
                dynamicTest("given $number when I convert to a Percentage I should get a $expected") {
                    assertEquals(expected, number.percent())
                }
            }

    @TestFactory
    fun `Given a Number and a precision When percent is called Then a precise Percentage is returned`() =
        percentageCreationWithPrecision
            .map { (number, precision, expected) ->
                dynamicTest("given $number when I convert to a Percentage with the precision $precision I should get $expected") {
                    assertEquals(expected, number.percent(precision))
                }
            }

    @TestFactory
    fun `Given a Number When toPercentage is called Then a Percentage is returned`() =
        percentageCreation
            .map { (number, expected) ->
                dynamicTest("given $number when I convert to a Percentage I should get a $expected") {
                    assertEquals(expected, number.toPercentage())
                }
            }

    @TestFactory
    fun `Given a Number and a precision When toPercentage is called Then a precise Percentage is returned`() =
        percentageCreationWithPrecision
            .map { (number, precision, expected) ->
                dynamicTest("given $number when I convert to a Percentage with the precision $precision I should get $expected") {
                    assertEquals(expected, number.toPercentage(precision))
                }
            }

    @TestFactory
    fun `Given a Number and a second number When ratioOf is called Then a Percentage of them is returned`() =
        Fixtures.ratioOf
            .map { (number, other, expected) ->
                dynamicTest("given $number when I pass $other then I should get $expected") {
                    assertEquals(expected, number.ratioOf(other))
                }
            }

    @TestFactory
    fun `Given a Number and a second number and a precision When ratioOf is called Then a Percentage of them is returned`() =
        Fixtures.ratioOfWithPrecision
            .map { (number, other, precision, expected) ->
                dynamicTest("given $number when I pass $other and $precision then I should get $expected") {
                    assertEquals(expected, number.ratioOf(other, precision))
                }
            }

    @TestFactory
    fun `Given a Number and a second number When relativeChange is called Then a Percentage change of them is returned`() =
        Fixtures.changeOf
            .map { (number, other, expected) ->
                dynamicTest("given $number when I pass $other then I should get $expected") {
                    assertEquals(expected, number.relativeChange(other))
                }
            }

    @TestFactory
    fun `Given a Number and a second number and a precision When relativeChange is called Then a precise Percentage change of them is returned`() =
        Fixtures.changeOfWithPrecision
            .map { (number, other, precision, expected) ->
                dynamicTest("given $number when I pass $other and $precision then I should get $expected") {
                    assertEquals(expected, number.relativeChange(other, precision))
                }
            }

    @TestFactory
    fun `Given a Number and a Percentage When of is called Then a proportional number is returned`() =
        Fixtures.numberOf
            .map { (percentage, number, expected) ->
                dynamicTest("given $number when I pass $percentage then I should get $expected") {
                    assertEquals(expected, number.of(percentage))
                }
            }

    @TestFactory
    fun `Given a Number and a Percentage When times is called Then a double is returned`() =
        Fixtures.times
            .map { (number, percentage, expected) ->
                dynamicTest("given $number when I multiply by $percentage then I should get $expected") {
                    assertEquals(expected, number * percentage)
                }
            }

    @TestFactory
    fun `Given a Number and a Percentage When plus is called Then a double is returned`() =
        Fixtures.plus
            .map { (number, percentage, expected) ->
                dynamicTest("given $number when I increase by $percentage then I should get $expected") {
                    assertEquals(expected, number + percentage)
                }
            }

    @TestFactory
    fun `Given a Number and a Percentage When minus is called Then a double is returned`() =
        Fixtures.subtraction
            .map { (number, percentage, expected) ->
                dynamicTest("given $number when I decrease by $percentage then I should get $expected") {
                    assertEquals(expected, number - percentage)
                }
            }
}