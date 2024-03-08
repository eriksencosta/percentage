package com.eriksencosta.percentage

import org.junit.jupiter.api.DynamicTest
import org.junit.jupiter.api.TestFactory
import kotlin.test.assertEquals

internal class UsageExamples {
    @Suppress("LongMethod")
    @TestFactory
    fun `Run expression using regular methods or extensions functions`() = listOf(
        Quadruple(
            "100 * 50%",
            { Percentage.of(50) * 100 },
            { 100 * 50.percent() },
            50.0
        ),
        Quadruple(
            "100 increase by 50%",
            { Percentage.of(50).increase(100) },
            { 100.increase(50.percent()) },
            150.0
        ),
        Quadruple(
            "100 decrease by 50%",
            { Percentage.of(50).decrease(100) },
            { 100.decrease(50.percent()) },
            50.0
        ),
        Quadruple(
            "percentage value of 1/4",
            { Percentage.ratioOf(1, 4) },
            { 1 ratioOf 4 },
            Percentage.of(25)
        ),
        Quadruple(
            "base value when 50% is 5",
            { Percentage.of(50).valueWhen(5) },
            { 5 valueWhen 50.percent() },
            10.0
        ),
        Quadruple(
            "relative change from 1 to 4",
            { Percentage.relativeChange(1, 4) },
            { 1 relativeChange 4 },
            Percentage.of(300)
        ),
        Quadruple(
            "50 * 50% increase by 25%",
            { (Percentage.of(50) * 50).increase(Percentage.of(25)) },
            { (50 * 50.percent()).increase(25.percent()) },
            31.25
        ),
        Quadruple(
            "300 * 125% decrease by 8%",
            { Percentage.of(8).decrease((Percentage.of(125) * 300)) },
            { (300 * 125.percent()).decrease(8.percent()) },
            345.0
        ),
        Quadruple(
            "33 increase by 5% then decrease by 5%",
            { Percentage.of(5).decrease((Percentage.of(5).increase(33))) },
            { 33.increase(5.percent()) decrease 5.percent() },
            32.9175
        ),
        Quadruple(
            "33 increase by 5[.3]% then decrease by 5[.1]%",
            { Percentage.of(5, 1).decrease((Percentage.of(5, 3).increase(33))) },
            { 33.increase(5.toPercentage(3)) decrease 5.toPercentage(1) },
            31.185
        ),
        Quadruple(
            "(33 increase by 5% decrease by 5%) * 10%",
            { Percentage.of(10) * (Percentage.of(5).decrease((Percentage.of(5).increase(33)))) },
            { (33.increase(5.percent()) decrease 5.percent()) * 10.percent() },
            3.29175
        ),
        Quadruple(
            "100 * (base value when 80% is 7) increase by 1/4 (25%)", // 7 is 80% of 8.75
            { (Percentage.ratioOf(1, 4)).increase(((Percentage.of(80).valueWhen(7)) * 100)) },
            { (100 * (7 valueWhen 80.percent())).increase((1 ratioOf 4)) },
            1093.75
        ),
        Quadruple(
            "100 * (base value when 80% is 7) increase by 1/4[.1] (30%)", // 7 is 80% of 8.75
            { (Percentage.ratioOf(1, 4, 1)).increase(((Percentage.of(80).valueWhen(7)) * 100)) },
            { (100 * (7 valueWhen 80.percent())).increase((1.ratioOf(4, 1))) },
            1137.5
        ),
        Quadruple(
            "(100 increase by relative change from 33 to 77) * 10[.2]%",
            { Percentage.of(10, 2) * (Percentage.relativeChange(33, 77).increase(100)) },
            { (100.increase((33 relativeChange 77))) * 10.percent(2) },
            23.333333333333332
        ),
        Quadruple(
            "(100 increase by relative change[.4] from 33 to 77) * 10[.2]%",
            { Percentage.of(10, 2) * (Percentage.relativeChange(33, 77, 4).increase(100)) },
            { (100.increase((33.relativeChange(77, 4)))) * 10.percent(2) },
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
