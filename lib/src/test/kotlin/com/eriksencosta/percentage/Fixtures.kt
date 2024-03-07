package com.eriksencosta.percentage

import java.math.RoundingMode

internal object Fixtures {
    private val piRounding = Math.PI * 100

    val creation = listOf(
        Pair(100.0, 1.0),
        Pair(50.0, 0.5),
        Pair(25.0, 0.25),
        Pair(0.0, 0.0),
        Pair(0.25, 0.0025),
        Pair(0.50, 0.005),
        Pair(1.1, 0.011000000000000001),
        Pair(100.0 / 3, 0.33333333333333337),
    )

    val preciseCreation = listOf(
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

    val accessors = listOf(
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

    val relativeChange = listOf(
        Triple(50, -250, Percentage.of(-600)),
        Triple(40, -80, Percentage.of(-300)),
        Triple(10, -10, Percentage.of(-200)),
        Triple(-20, -40, Percentage.of(-100)),
        Triple(20, 10, Percentage.of(-50)),
        Triple(-10, -10, Percentage.of(0)),
        Triple(10, 15, Percentage.of(50)),
        Triple(10, 20, Percentage.of(100)),
        Triple(-20, 20, Percentage.of(200)),
        Triple(-10, 20, Percentage.of(300)),
        Triple(-5, 25, Percentage.of(600)),

        // Zero cases
        Triple(-10, 0, Percentage.of(100)),
        Triple(10, 0, Percentage.of(-100)),
        Triple(0, 0, Percentage.of(0)),
    )

    val relativeChangeWithPrecision = listOf(
        Quadruple(50, -250, 1, Percentage.of(-600, 1)),
        Quadruple(40, -80, 2, Percentage.of(-300, 2)),
        Quadruple(10, -10, 3, Percentage.of(-200, 3)),
        Quadruple(-20, -40, 4, Percentage.of(-100, 4)),
        Quadruple(20, 10, 5, Percentage.of(-50, 5)),
        Quadruple(-10, -10, 6, Percentage.of(0, 6)),
        Quadruple(10, 15, 7, Percentage.of(50, 7)),
        Quadruple(10, 20, 8, Percentage.of(100, 8)),
        Quadruple(-20, 20, 9, Percentage.of(200, 9)),
        Quadruple(-10, 20, 10, Percentage.of(300, 10)),
        Quadruple(-5, 25, 11, Percentage.of(600, 11)),

        // Zero cases
        Quadruple(-10, 0, 12, Percentage.of(100, 12)),
        Quadruple(10, 0, 13, Percentage.of(-100, 13)),
        Quadruple(0, 0, 14, Percentage.of(0, 14)),
    )

    val ratioOf = listOf(
        Triple(0, 1, Percentage.of(0)),
        Triple(1, 2, Percentage.of(50)),
        Triple(1, -2, Percentage.of(-50)),
        Triple(-1, -2, Percentage.of(50)),
    )

    val ratioOfWithPrecision = listOf(
        Quadruple(1, 2, 1, Percentage.of(50, 1)),
        Quadruple(1, -2, 2, Percentage.of(-50, 2)),
        Quadruple(-1, -2, 3, Percentage.of(50, 3)),
    )

    val valueWhen = listOf(
        Triple(Percentage.of(125), 25, 20.0),
        Triple(Percentage.of(60), 45, 75.0),
        Triple(Percentage.of(10), 10, 100.0),
        Triple(Percentage.of(20), 25, 125.0),

        Triple(Percentage.of(-125), 25, -20.0),
        Triple(Percentage.of(-60), 45, -75.0),
        Triple(Percentage.of(-10), 10, -100.0),
        Triple(Percentage.of(-20), 25, -125.0),

        Triple(Percentage.of(-125), -25, 20.0),
        Triple(Percentage.of(-60), -45, 75.0),
        Triple(Percentage.of(-10), -10, 100.0),
        Triple(Percentage.of(-20), -25, 125.0),

        Triple(Percentage.of(10), 0, 0.0),
        Triple(Percentage.of(50), 0, 0.0),
        Triple(Percentage.of(100), 0, 0.0),
    )

    val times = listOf(
        Triple(100, Percentage.of(100), 100.0),
        Triple(500, Percentage.of(20), 100.0),
        Triple(1, Percentage.of(-100), -1.0),
        Triple(100, Percentage.of(0), 0.0),
        Triple(0, Percentage.of(100), 0.0),
    )

    val plus = listOf(
        Triple(100, Percentage.of(100), 200.0),
        Triple(500, Percentage.of(20), 600.0),
        Triple(1, Percentage.of(-100), 0.0),
        Triple(100, Percentage.of(0), 100.0),
        Triple(0, Percentage.of(100), 0.0),
    )

    val subtraction = listOf(
        Triple(100, Percentage.of(10), 90.0),
        Triple(500, Percentage.of(20), 400.0),
        Triple(1, Percentage.of(-100), 2.0),
        Triple(100, Percentage.of(0), 100.0),
        Triple(0, Percentage.of(100), 0.0),
    )
}

internal data class AccessorsTestTable(
    val number: Number,
    val isZero: Boolean,
    val isNotZero: Boolean,
    val isPositive: Boolean,
    val isPositiveOrZero: Boolean,
    val isNegative: Boolean,
    val isNegativeOrZero: Boolean
)

internal data class Quadruple<out A, out B, out C, out D>(val first: A, val second: B, val third: C, val fourth: D) {
    override fun toString(): String = "($first, $second, $third, $fourth)"
}
