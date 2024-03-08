package com.eriksencosta.percentage

import java.util.Objects
import kotlin.math.abs

/**
 * Represents a [percentage](https://en.wikipedia.org/wiki/Percentage) number, a numerical value divided by 100.
 *
 * To create a `Percentage`, just pass the desired percentage value to its factory method. For example, to create a
 * `Percentage` for 25%, do:
 *
 *     val percentage = Percentage.of(25) // That's 25%
 *
 * Then you can do percentage calculations like multiplication, increase, and decrease:
 *
 *     val number = 100
 *     percentage * number        // Results: 25.0
 *     percentage increase number // Results: 125.0
 *     percentage decrease number // Results: 75.0
 *
 * Extension functions make the `Percentage` class easier to use:
 *
 *     val number = 100
 *     number * 10.percent()        // Results: 10.0
 *     number increase 25.percent() // Results: 110.0
 *
 * You can also query the `Percentage` value (the original number you passed to the factory method) and its decimal
 * value (the value used for the calculations):
 *
 *     percentage.value   // Results: 50.0
 *     percentage.decimal // Results: 0.5
 *
 * You can also determine the precision used to round the `Percentage`. This way, you can make percentage calculations
 * in accordance to your needs/policies:
 *
 *     val imprecise = Percentage.of(100.0 / 3)
 *     val precise = Percentage.of(100.0 / 3, 2)
 *
 *     println(imprecise)           // Prints: 33.333333%
 *     println(precise)             // Prints: 33.33%
 *
 *     val number = 27
 *     println(imprecise * number)  // Prints: 9.000000000000002
 *     println(precise * number)    // Prints: 8.91
 *
 * By default, the rounding mode used to round the value is [java.math.RoundingMode.HALF_UP]. If you need to use
 * another mode, use the factory method which receives [Rounding] as an argument:
 *
 *     val percentage = Percentage.of(50, Rounding.of(2, RoundingMode.HALF_DOWN))
 *
 * The `Percentage` class is immutable and thread-safe.
 *
 * @param[value]    The numerical value of the [Percentage].
 * @param[rounding] The [Rounding] strategy to round the decimal representation of the [Percentage].
 */
@Suppress("TooManyFunctions")
class Percentage private constructor(value: Number, private val rounding: Rounding) : Comparable<Percentage> {
    /**
     * The percentage value.
     */
    val value: Double = value.toDouble()

    /**
     * The percentage value divided by 100 and rounded using [rounding].
     */
    val decimal: Double = rounding.round(this.value / PERCENT)

    /**
     * true if the `Percentage` is zero.
     */
    val isZero: Boolean = 0.0 == decimal

    /**
     * true if the `Percentage` is not zero.
     */
    val isNotZero: Boolean = !isZero

    /**
     * true if the `Percentage` is positive.
     */
    val isPositive: Boolean = 0 < decimal

    /**
     * true if the `Percentage` is positive or zero.
     */
    val isPositiveOrZero: Boolean = isPositive || isZero

    /**
     * true if the `Percentage` is negative.
     */
    val isNegative: Boolean = 0 > decimal

    /**
     * true if the `Percentage` is negative or zero.
     */
    val isNegativeOrZero: Boolean = isNegative || isZero

    companion object {
        private const val PERCENT: Double = 100.0

        /**
         * Creates a `Percentage` based on a number.
         *
         * @return A [Percentage].
         */
        fun of(value: Number): Percentage = of(value, Rounding.default())

        /**
         * Creates a `Percentage` based on a number and rounded according to the given `precision`.
         *
         * @param[precision] The precision scale to round the decimal (value / 100) representation of the [Percentage].
         *   The rounding is done using [ImpreciseRounding] policy (i.e. rounds using [java.math.RoundingMode.HALF_UP]
         *   policy).
         *
         * @return A [Percentage] rounded according to [precision].
         */
        fun of(value: Number, precision: Int): Percentage = of(value, Rounding.of(precision))

        /**
         * Creates a `Percentage` based on a number and rounded according to the given `rounding` strategy.
         *
         * @param[rounding] The [Rounding] strategy to round the decimal representation of the [Percentage].
         *
         * @return A [Percentage] rounded according to [rounding].
         */
        fun of(value: Number, rounding: Rounding): Percentage = Percentage(value, rounding)

        /**
         * Creates a `Percentage` based on the ratio of two numbers.
         *
         * Example:
         *
         *     val x = Percentage.ratioOf(1, 5)
         *     println(x) // Prints: 20.000000%
         *
         * @param[number] The first number.
         * @param[other]  The second number.
         *
         * @throws[IllegalArgumentException] When `other` is zero.
         *
         * @return A [Percentage] that represents the ratio of [number] and [other].
         */
        fun ratioOf(number: Number, other: Number): Percentage = ratioOf(number, other, Rounding.default())

        /**
         * Creates a `Percentage` based on the ratio of two numbers and rounded according to the given `precision`.
         *
         * Example:
         *
         *     val x = Percentage.ratioOf(1, 5, 2)
         *     println(x) // Prints: 20.00%
         *
         * @param[number]    The first number.
         * @param[other]     The second number.
         * @param[precision] The precision scale to round the decimal (value / 100) representation of the [Percentage].
         *   The rounding is done using [ImpreciseRounding] policy (i.e. rounds using [java.math.RoundingMode.HALF_UP]
         *   policy).
         *
         * @throws[IllegalArgumentException] When `other` is zero.
         *
         * @return A [Percentage] that represents the ratio of [number] and [other].
         */
        fun ratioOf(number: Number, other: Number, precision: Int): Percentage =
            ratioOf(number, other, Rounding.of(precision))

        /**
         * Creates a `Percentage` based on the ratio of two numbers and rounded according to the given `rounding`
         * strategy.
         *
         * Example:
         *
         *     val x = Percentage.ratioOf(1, 5, Rounding.of(2, RoundingMode.HALF_DOWN))
         *     println(x) // Prints: 20.00%
         *
         * @param[number]   The first number.
         * @param[other]    The second number.
         * @param[rounding] The [Rounding] strategy to round the decimal representation of the [Percentage].
         *
         * @return A [Percentage] that represents the ratio of [number] and [other].
         */
        fun ratioOf(number: Number, other: Number, rounding: Rounding): Percentage =
            require(0 != other) { "The argument \"other\" can not be zero" }.run {
                of(number.toDouble() / other.toDouble() * PERCENT, rounding)
            }

        /**
         * Creates a `Percentage` which represents the relative change of an initial and ending numbers.
         *
         * Example:
         *
         *     val x = Percentage.relativeChange(1, 5)
         *     println(x) // Prints: 400.000000%
         *
         * When the initial number is zero, an `ArgumentCanNotBeZero` exception is thrown as the relative change for
         * this case [is not defined](https://en.wikipedia.org/wiki/Relative_change).
         *
         * When both numbers are zero, a `Percentage(0)` is returned meaning no change happened instead of returning a
         * `Percentage(NaN)` (which would happen as a result of dividing 0 by 0, both computationally and
         * [mathematically](https://www.math.utah.edu/~pa/math/0by0.html)).
         *
         * @param[initial] The initial number.
         * @param[ending]  The ending number.
         *
         * @throws[IllegalArgumentException] When `initial` number is zero.
         *
         * @return A [Percentage] that represents the percentage change of an initial and ending numbers.
         */
        fun relativeChange(initial: Number, ending: Number): Percentage =
            relativeChange(initial, ending, Rounding.default())

        /**
         * Creates a `Percentage` which represents the relative change of an initial and ending numbers.
         *
         * Example:
         *
         *     val x = Percentage.relativeChange(1, 5, 2)
         *     println(x) // Prints: 400.00%
         *
         * When the initial number is zero, an `ArgumentCanNotBeZero` exception is thrown as the relative change for
         * this case [is not defined](https://en.wikipedia.org/wiki/Relative_change).
         *
         * When both numbers are zero, a `Percentage(0)` is returned meaning no change happened instead of returning a
         * `Percentage(NaN)` (which would happen as a result of dividing 0 by 0, both computationally and
         * [mathematically](https://www.math.utah.edu/~pa/math/0by0.html)).
         *
         * @param[initial]   The initial number.
         * @param[ending]    The ending number.
         * @param[precision] The precision scale to round the decimal (value / 100) representation of the [Percentage].
         *   The rounding is done using [ImpreciseRounding] policy (i.e. rounds using [java.math.RoundingMode.HALF_UP]
         *   policy).
         *
         * @throws[IllegalArgumentException] When `initial` is zero.
         *
         * @return A [Percentage] that represents the percentage change of an initial and ending numbers.
         */
        fun relativeChange(initial: Number, ending: Number, precision: Int): Percentage =
            relativeChange(initial, ending, Rounding.of(precision))

        /**
         * Creates a `Percentage` which represents the relative change of an initial and ending numbers.
         *
         * Example:
         *
         *     val x = Percentage.relativeChange(1, 5, Rounding.of(2, RoundingMode.HALF_DOWN))
         *     println(x) // Prints: 400.00%
         *
         * When the initial number is zero, an `ArgumentCanNotBeZero` exception is thrown as the relative change for
         * this case [is not defined](https://en.wikipedia.org/wiki/Relative_change).
         *
         * When both numbers are zero, a `Percentage(0)` is returned meaning no change happened instead of returning a
         * `Percentage(NaN)` (which would happen as a result of dividing 0 by 0, both computationally and
         * [mathematically](https://www.math.utah.edu/~pa/math/0by0.html)).
         *
         * @param[initial]  The initial number.
         * @param[ending]   The ending number.
         * @param[rounding] The [Rounding] strategy to round the decimal representation of the [Percentage].
         *
         * @throws[IllegalArgumentException] When `initial` is zero.
         *
         * @return A [Percentage] that represents the percentage change of an initial and ending numbers.
         */
        fun relativeChange(initial: Number, ending: Number, rounding: Rounding): Percentage = when {
            0 == initial && 0 == ending -> of(0, rounding)
            else -> {
                require(0 != initial) { "The argument \"initial\" can not be zero" }

                val initialValue = initial.toDouble()
                of((ending.toDouble() - initialValue) / abs(initialValue) * PERCENT, rounding)
            }
        }
    }

    /**
     * Creates a `Percentage` based on this one with a new precision.
     *
     * @param[precision] The precision scale to round the decimal (value / 100) representation of the [Percentage]. The
     *   rounding is done using [ImpreciseRounding] policy (i.e. rounds using [java.math.RoundingMode.HALF_UP] policy).
     *
     * @return A [Percentage] with the precision scale.
     */
    infix fun with(precision: Int): Percentage = with(rounding.with(precision))

    /**
     * Creates a `Percentage` based on this one with a new rounding strategy.
     *
     * @param[rounding] The [Rounding] strategy to round the decimal representation of the [Percentage].
     *
     * @return A [Percentage] with the rounding strategy.
     */
    infix fun with(rounding: Rounding): Percentage = of(value, rounding)

    /**
     * Calculates the base value of a number for the current `Percentage`. This method helps to answer the question:
     * "5 is 20% of what number?" Example:
     *
     *     Percentage.of(20).valueWhen(5) // Results: 25.0 as 5 is 20% of 25
     *
     * @param[number] The number to find its base value when representing this [Percentage].
     *
     * @throws[IllegalStateException] When this `Percentage` value is zero.
     *
     * @return The number that the given number represents as the current [Percentage].
     */
    infix fun valueWhen(number: Number): Double =
        check(0.0 != decimal) { "This operation can not execute when Percentage is zero" }.run {
            number.toDouble() / decimal
        }

    /**
     * Creates a positive `Percentage` based on this one.
     *
     * @return A positive [Percentage] object.
     */
    operator fun unaryPlus(): Percentage = if (isPositive) this else of(value * -1, rounding)

    /**
     * Creates a `Percentage` after negating this one.
     *
     * @return A [Percentage] object with the negation applied.
     */
    operator fun unaryMinus(): Percentage = of(value * -1, rounding)

    /**
     * Multiplies this `Percentage` by a number.
     *
     * @param[number] A number.
     *
     * @return The resulting value.
     */
    operator fun times(number: Number): Double = number.toDouble() * decimal

    /**
     * Increases a number by this `Percentage`.
     *
     * @param[number] A number.
     *
     * @return The resulting value.
     */
    operator fun plus(number: Number): Double = number.toDouble().let { whole ->
        whole + whole * decimal
    }

    /**
     * Decreases a number by this `Percentage`.
     *
     * @param[number] A number.
     *
     * @return The resulting value.
     */
    operator fun minus(number: Number): Double = number.toDouble().let { whole ->
        whole - whole * decimal
    }

    override fun compareTo(other: Percentage): Int = decimal.compareTo(other.decimal) // we disregard the precision

    override fun equals(other: Any?): Boolean = this === other ||
        (other is Percentage && value == other.value && decimal == other.decimal && rounding == other.rounding)

    override fun hashCode(): Int = Objects.hash(decimal, rounding)

    override fun toString(): String = "${rounding.roundingFormat()}%%".format(value)
}
