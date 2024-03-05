package com.eriksencosta.percentage

object Fixtures {
    val relativeChange = listOf(
        Triple(50, -250, Percentage(-600)),
        Triple(40, -80, Percentage(-300)),
        Triple(10, -10, Percentage(-200)),
        Triple(-20, -40, Percentage(-100)),
        Triple(20, 10, Percentage(-50)),
        Triple(-10, -10, Percentage(0)),
        Triple(10, 15, Percentage(50)),
        Triple(10, 20, Percentage(100)),
        Triple(-20, 20, Percentage(200)),
        Triple(-10, 20, Percentage(300)),
        Triple(-5, 25, Percentage(600)),

        // Zero cases
        Triple(-10, 0, Percentage(100)),
        Triple(10, 0, Percentage(-100)),
        Triple(0, 0, Percentage(0)),
    )

    val relativeChangeWithPrecision = listOf(
        Quadruple(50, -250, 1, Percentage(-600, 1)),
        Quadruple(40, -80, 2, Percentage(-300, 2)),
        Quadruple(10, -10, 3, Percentage(-200, 3)),
        Quadruple(-20, -40, 4, Percentage(-100, 4)),
        Quadruple(20, 10, 5, Percentage(-50, 5)),
        Quadruple(-10, -10, 6, Percentage(0, 6)),
        Quadruple(10, 15, 7, Percentage(50, 7)),
        Quadruple(10, 20, 8, Percentage(100, 8)),
        Quadruple(-20, 20, 9, Percentage(200, 9)),
        Quadruple(-10, 20, 10, Percentage(300, 10)),
        Quadruple(-5, 25, 11, Percentage(600, 11)),

        // Zero cases
        Quadruple(-10, 0, 12, Percentage(100, 12)),
        Quadruple(10, 0, 13, Percentage(-100, 13)),
        Quadruple(0, 0, 14, Percentage(0, 14)),
    )

    val ratioOf = listOf(
        Triple(1, 2, Percentage(50)),
        Triple(1, -2, Percentage(-50)),
        Triple(-1, -2, Percentage(50)),
    )

    val ratioOfWithPrecision = listOf(
        Quadruple(1, 2, 1, Percentage(50, 1)),
        Quadruple(1, -2, 2, Percentage(-50, 2)),
        Quadruple(-1, -2, 3, Percentage(50, 3)),
    )

    val valueWhen = listOf(
        Triple(Percentage(125), 25, 20.0),
        Triple(Percentage(60), 45, 75.0),
        Triple(Percentage(10), 10, 100.0),
        Triple(Percentage(20), 25, 125.0),

        Triple(Percentage(-125), 25, -20.0),
        Triple(Percentage(-60), 45, -75.0),
        Triple(Percentage(-10), 10, -100.0),
        Triple(Percentage(-20), 25, -125.0),

        Triple(Percentage(-125), -25, 20.0),
        Triple(Percentage(-60), -45, 75.0),
        Triple(Percentage(-10), -10, 100.0),
        Triple(Percentage(-20), -25, 125.0),

        Triple(Percentage(10), 0, 0.0),
        Triple(Percentage(50), 0, 0.0),
        Triple(Percentage(100), 0, 0.0),
    )

    val times = listOf(
        Triple(100, Percentage(100), 100.0),
        Triple(500, Percentage(20), 100.0),
        Triple(1, Percentage(-100), -1.0),
        Triple(100, Percentage(0), 0.0),
        Triple(0, Percentage(100), 0.0),
    )

    val plus = listOf(
        Triple(100, Percentage(100), 200.0),
        Triple(500, Percentage(20), 600.0),
        Triple(1, Percentage(-100), 0.0),
        Triple(100, Percentage(0), 100.0),
        Triple(0, Percentage(100), 0.0),
    )

    val subtraction = listOf(
        Triple(100, Percentage(10), 90.0),
        Triple(500, Percentage(20), 400.0),
        Triple(1, Percentage(-100), 2.0),
        Triple(100, Percentage(0), 100.0),
        Triple(0, Percentage(100), 0.0),
    )
}