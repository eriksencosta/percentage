package com.eriksencosta.percentage

import org.junit.jupiter.api.DynamicTest
import org.junit.jupiter.api.TestFactory
import kotlin.test.assertEquals

internal class ExpressionsExamples {
    @Suppress("LongMethod")
    @TestFactory
    fun `Given an expression When I run it Then a number is calculated`() = listOf(
        Quadruple(
            "50 * 50% + 25%",
            { Percentage(50) * 50 + Percentage(25) },
            { 50 * 50.percent() + 25.percent() },
            31.25
        ),
        Quadruple(
            "300 * 125% - 8%",
            { Percentage(8) - (Percentage(125) * 300) },
            { 300 * 125.percent() - 8.percent() },
            345.0
        ),
        Quadruple(
            "33 + 5% - 5%",
            { Percentage(5) - (Percentage(5) + 33) },
            { 33 + 5.percent() - 5.percent() },
            32.9175
        ),
        Quadruple(
            "33 + 5[.3]% - 5[.1]%",
            { Percentage(5, 1) - (Percentage(5, 3) + 33) },
            { 33 + 5.toPercentage(3) - 5.toPercentage(1) },
            31.185
        ),
        Quadruple(
            "100 * (base value of 7 when 80%) + 1/4 (25%)", // 7 is 80% of 8.75
            { (Percentage.ratioOf(1, 4)) + ((Percentage(80).valueWhen(7)) * 100) },
            { 100 * (7 valueWhen 80.percent()) + (1 ratioOf 4) },
            1093.75
        ),
        Quadruple(
            "100 * (base value of 7 when 80%) + 1/4[.1] (30%)", // 7 is 80% of 8.75
            { (Percentage.ratioOf(1, 4, 1)) + ((Percentage(80).valueWhen(7)) * 100) },
            { 100 * (7 valueWhen 80.percent()) + (1.ratioOf(4, 1)) },
            1137.5
        ),
        Quadruple(
            "(33 + 5% - 5%) * 10%",
            { Percentage(10) * (Percentage(5) - (Percentage(5) + 33)) },
            { (33 + 5.percent() - 5.percent()) * 10.percent() },
            3.29175
        ),
        Quadruple(
            "(100 + relative % change of 33 to 77) * 10%",
            { Percentage(10, 2) * (Percentage.relativeChange(33, 77) + 100) },
            { (100 + (33 relativeChange 77)) * 10.percent(2) },
            23.333333333333332
        ),
        Quadruple(
            "(100 + relative %[.4] change of 33 to 77) * 10%",
            { Percentage(10, 2) * (Percentage.relativeChange(33, 77, 4) + 100) },
            { (100 + (33.relativeChange(77, 4))) * 10.percent(2) },
            23.333
        )
    )
        .map { (formula, regularMethods, extensionFunctions, expected) ->
            DynamicTest.dynamicTest(
                "given an expression for the formula $formula when I run it then I should get $expected"
            ) {
                assertEquals(expected, regularMethods())
                assertEquals(expected, extensionFunctions())
            }
        }
}
